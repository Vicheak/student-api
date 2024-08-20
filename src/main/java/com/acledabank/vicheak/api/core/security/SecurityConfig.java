package com.acledabank.vicheak.api.core.security;

import com.acledabank.vicheak.api.core.util.KeyUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final KeyUtil keyUtil;

    @Value("${api.students}")
    private String studentApiEndpoint;
    @Value("${api.users}")
    private String userApiEndpoint;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProviderConfig() throws JOSEException {
        return new JwtAuthenticationProvider(jwtRefreshTokenDecoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChainConfig(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers(
                    // Swagger UI v2
                    "/v2/api-docs",
                    "/swagger-resources",
                    "/swagger-resources/**",
                    "/configuration/ui",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**",
                    // Swagger UI v3 (OpenAPI)
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/api/v1/auth/**",
                    "/api",
                    "/api/v1/chat").permitAll();

            auth.requestMatchers(HttpMethod.POST, studentApiEndpoint).hasAuthority(SecurityScope.ADMIN.getScope());
            auth.requestMatchers(HttpMethod.DELETE, studentApiEndpoint).hasAuthority(SecurityScope.ADMIN.getScope());

            auth.requestMatchers(HttpMethod.GET, "/api/v1/users/me").hasAnyAuthority(SecurityScope.ADMIN.getScope(), SecurityScope.STAFF.getScope());
            auth.requestMatchers(HttpMethod.GET, userApiEndpoint).hasAuthority(SecurityScope.ADMIN.getScope());
            auth.requestMatchers(HttpMethod.POST, userApiEndpoint).hasAuthority(SecurityScope.ADMIN.getScope());
            auth.requestMatchers(HttpMethod.PUT, "/api/v1/users/change-password/**").hasAnyAuthority(SecurityScope.ADMIN.getScope(), SecurityScope.STAFF.getScope());
            auth.requestMatchers(HttpMethod.PUT, userApiEndpoint).hasAuthority(SecurityScope.ADMIN.getScope());
            auth.requestMatchers(HttpMethod.PATCH, userApiEndpoint).hasAnyAuthority(SecurityScope.ADMIN.getScope(), SecurityScope.STAFF.getScope());
            auth.requestMatchers(HttpMethod.DELETE, userApiEndpoint).hasAuthority(SecurityScope.ADMIN.getScope());

            auth.anyRequest().authenticated();
        });

        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    //access token beans
    @Bean
    @Primary
    public JWKSource<SecurityContext> jwkSource() {
        JWK jwk = new RSAKey.Builder(keyUtil.getAccessTokenPublicKey())
                .privateKey(keyUtil.getAccessTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();

        var jwkSet = new JWKSet(jwk);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtil.getAccessTokenPublicKey()).build();
    }

    @Bean
    @Primary
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    //refresh token beans
    @Bean("refreshTokenJwkSource")
    public JWKSource<SecurityContext> refreshTokenJwkSource() {
        JWK jwk = new RSAKey.Builder(keyUtil.getRefreshTokenPublicKey())
                .privateKey(keyUtil.getRefreshTokenPrivateKey())
                .keyID(UUID.randomUUID().toString())
                .build();

        var jwkSet = new JWKSet(jwk);
        return (jwkSelector, context) -> jwkSelector.select(jwkSet);
    }

    @Bean("jwtRefreshTokenDecoder")
    public JwtDecoder jwtRefreshTokenDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyUtil.getRefreshTokenPublicKey()).build();
    }

    @Bean("jwtRefreshTokenEncoder")
    public JwtEncoder jwtRefreshTokenEncoder(@Qualifier("refreshTokenJwkSource") JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

}
