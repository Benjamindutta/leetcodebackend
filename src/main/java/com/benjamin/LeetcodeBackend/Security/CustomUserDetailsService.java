package com.benjamin.LeetcodeBackend.Security;

import com.benjamin.LeetcodeBackend.collection.User;
import com.benjamin.LeetcodeBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails customUserDetails=new CustomUserDetails();
        User user=userService.getUserbyUsername(username);
        customUserDetails.setUsername(user.getUsername());
        customUserDetails.setLeetcodeUsername(user.getLeetcodeUsername());
        return customUserDetails;
    }
}
