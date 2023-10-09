package com.benjamin.LeetcodeBackend.Security;

import com.benjamin.LeetcodeBackend.collection.Question;
import com.benjamin.LeetcodeBackend.collection.User;
import com.benjamin.LeetcodeBackend.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashSet;

@RequiredArgsConstructor
@Component
@Slf4j
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    private String redirectUri="https://leetcode-frontend-tracker.vercel.app/authorized";
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        logger.info(authentication);
        handle(request, response, authentication);
        super.clearAuthenticationAttributes(request);
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        logger.info(request.getRequestURL());
        String targetUrl =  redirectUri.isEmpty()?determineTargetUrl(request, response, authentication):redirectUri ;
//        String targetUrl=request.getParameter("redirect_uri");
        logger.info(targetUrl);


        if(authentication.getPrincipal() instanceof DefaultOAuth2User){
            DefaultOAuth2User userDetails=(DefaultOAuth2User) authentication.getPrincipal();
            String username=userDetails.getAttribute("login");
            if(userService.getUserbyUsername(username)==null){
                User user=new User();
                user.setUsername(username);
                user.setQuestionsToRevised(new HashSet<Question>());
                user.setQuestionsSolved(new HashSet<Question>());
                user.setLeetcodeUsername("*");
                userService.save(user);
            }

        }
        String token= tokenProvider.generate(authentication);
        targetUrl = UriComponentsBuilder.fromUriString(targetUrl).queryParam("token", token).build().toUriString();
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
