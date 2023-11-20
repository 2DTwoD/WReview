package org.study.wreview.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.study.wreview.models.Person;


@Component
public class CurrentUserInfo {
    public String getUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public boolean userIsCurrent(String name){
        return name != null && name.equals(getUsername());
    }
}
