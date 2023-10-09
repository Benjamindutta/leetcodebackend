package com.benjamin.LeetcodeBackend.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

            try{
                getJwtFromRequest(request)
                        .flatMap(tokenProvider::validateTokenAndGetJws)
                        .ifPresent(jws -> {
                            String username = jws.getBody().getSubject();
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        });
            }catch (Exception e){
                log.error("Cannot set user authentication", e);
            }
            filterChain.doFilter(request,response);
    }

    private Optional<String> getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        logger.info(cookies.length);
        logger.info(cookies[0].getValue()+"  "+cookies[0].getName());
        String token=null;
        if (cookies != null&&cookies.length!=0) {
            // Log the cookies
            List<Cookie> filtered= Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("j_w_t"))
                    .collect(Collectors.toList());

            token=filtered.get(0).getValue();
        }
//        String tokenHeader = request.getHeader(TOKEN_HEADER);
//        logger.info(tokenHeader);
        if (StringUtils.hasText(token) && !token.equals(null)) {
            return Optional.of(token);
        }
        return Optional.empty();
    }

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}