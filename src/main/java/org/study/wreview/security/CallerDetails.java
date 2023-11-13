package org.study.wreview.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.study.wreview.models.Caller;

import java.util.Collection;
import java.util.Collections;

public class CallerDetails implements UserDetails {
    final private Caller caller;

    public CallerDetails(Caller caller) {
        this.caller = caller;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(caller.getRole()));
    }

    @Override
    public String getPassword() {
        return caller.getPassword();
    }

    @Override
    public String getUsername() {
        return caller.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return caller.isEnabled();
    }
}
