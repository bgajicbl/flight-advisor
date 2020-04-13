package com.bojan.flightadvisor.authentication;

import com.bojan.flightadvisor.entity.CustomUser;
import com.bojan.flightadvisor.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserDetailsMapper {

    public UserDetails toUserDetails(CustomUser customUser) {

        return User.withUsername(customUser.getUsername())
                .password(customUser.getPassword())
                .authorities(getAuthorities(customUser))
                .build();
    }

    private Set<GrantedAuthority> getAuthorities(CustomUser user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}