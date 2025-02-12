package io.joework.malabaakapi.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.joework.malabaakapi.config.security.password.PasswordConfig;
import io.joework.malabaakapi.service.ApplicationUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Import({PasswordConfig.class, ApplicationUserDetailsService.class})
public class WebSecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {"/auth/**", "/verify", "/login"};
    private static final String TOKEN_ENDPOINT = "/auth/token";
    private final RSAKeysProperties rsaKeysProperties;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserDetailsService applicationUserDetailsService;
    private final CustomBasicAuthEntryPoint customBasicAuthEntryPoint;
    private final CustomJwtTokenAuthenticationEntryPoint customJwtTokenAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .oauth2ResourceServer(config -> config.authenticationEntryPoint(customJwtTokenAuthenticationEntryPoint)
                        .jwt(Customizer.withDefaults()))
                .exceptionHandling(
                        (ex) -> ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .build();
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain tokenSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(new AntPathRequestMatcher(TOKEN_ENDPOINT))
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .httpBasic(config -> config.authenticationEntryPoint(customBasicAuthEntryPoint))
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeysProperties.publicKey()).privateKey(rsaKeysProperties.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeysProperties.publicKey())
                .build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(applicationUserDetailsService).passwordEncoder(passwordEncoder);

    }
}
