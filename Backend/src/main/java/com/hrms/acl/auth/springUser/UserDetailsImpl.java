package com.hrms.acl.auth.springUser;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hrms.acl.auth.entity.Authority;
import com.hrms.acl.auth.entity.CustomAuthorityDeserializer;
import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UserDetailsImpl implements UserDetails {

    private User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }


    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<Role> roles = this.user.getRoles();
        Set<Authority> set=new HashSet<>();

        roles.forEach(role -> {
            set.add(new Authority(role.getRoleName()));
        });
        return set;

    }

    public User getUser() {
        return user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.user.getAccountExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.getAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.user.getPasswordExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.getEnabled();
    }



}
