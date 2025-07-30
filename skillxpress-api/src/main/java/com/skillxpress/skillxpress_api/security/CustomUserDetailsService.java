package com.skillxpress.skillxpress_api.security;



import com.skillxpress.skillxpress_api.model.User;
import com.skillxpress.skillxpress_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service                     // registers the bean
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User u = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPasswordHash())
                .authorities(
                        // convert enum to ROLE_…
                        "ROLE_" + u.getRole().name()
                )
                .accountExpired(false)
                .accountLocked(!u.isEnabled())
                .credentialsExpired(false)
                .disabled(!u.isEnabled())
                .build();
    }
}

