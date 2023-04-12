package com.erymanthian.dance.security;

import com.erymanthian.dance.entities.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SecurityUser implements UserDetails {
    private final transient User user;

    @Override
    public String getUsername() {
        return user.getEmail() == null ? user.getPhoneNumber() : user.getEmail();
    }

    public Long getUserId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user::getRole);
    }

    @Override
    public boolean isEnabled() {
        return user.getStep() != 0;
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
}
