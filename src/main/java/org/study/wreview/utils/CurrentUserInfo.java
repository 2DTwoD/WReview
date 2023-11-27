package org.study.wreview.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.study.wreview.models.Person;


public class CurrentUserInfo {
    public static String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public static boolean userIsCurrent(String name){
        return name != null && name.equals(getUsername());
    }

    public static boolean currentUserIsAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities()
                .stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }
}
