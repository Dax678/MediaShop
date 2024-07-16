package org.example.mediashop.Configuration.Security;

import lombok.AllArgsConstructor;
import org.example.mediashop.Data.Entity.ERole;
import org.example.mediashop.Service.UserDetailsServiceImpl;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableCaching
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final AuthEntryPointJwt unauthorizedHandler;

    private final UserDetailsServiceImpl authService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                        "/api/v1/auth/signup",
                                        "/api/v1/auth/login",
                                        "/api/v1/auth/roleTest/guest" // Guest role test endpoint
                                ).permitAll()
                                .requestMatchers(
                                        "/api/v1/auth/roleTest/user" // User role test endpoint
                                ).hasRole(ERole.USER.getRoleNameWithoutPrefix())
                                .requestMatchers(
                                        "/api/v1/products/new",
                                        "/api/v1/discounts/new",
                                        "/api/v1/orders/new",
                                        "/api/v1/auth/roleTest/admin" // Admin role test endpoint
                                ).hasRole(ERole.ADMIN.getRoleNameWithoutPrefix())
                                .requestMatchers(
                                        "/api/v1/products/id/{id}",
                                        "/api/v1/products/name/{name}",
                                        "/api/v1/products/category/{category}",
                                        "/api/v1/categories",
                                        "/api/v1/categories/id/{id}",
                                        "/api/v1/categories/title/{title}",
                                        "/api/v1/discounts/id/{id}",
                                        "/api/v1/orders/id/{id}",
                                        "/api/order/id/{id}/status/{status}",
                                        "/api/v1/orders/status/{status}",
                                        "/api/v1/orders/status/{status}/userId/{userId}"
                                ).hasAnyRole(ERole.USER.getRoleNameWithoutPrefix(), ERole.ADMIN.getRoleNameWithoutPrefix())
                                .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }
}
