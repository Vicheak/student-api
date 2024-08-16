package com.acledabank.vicheak.api.core.security;

import com.acledabank.vicheak.api.core.entity.User;
import com.acledabank.vicheak.api.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User authenticatedUser = userRepository.findByEmail(username).orElseThrow(
                () -> {
                    log.error("Email has not been found!");
                    return new UsernameNotFoundException("Email has not been found!");
                }
        );
        return new CustomUserDetails(authenticatedUser);
    }

}
