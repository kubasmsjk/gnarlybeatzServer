package pl.gnarlybeatz.gnarlybeatzServer.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static pl.gnarlybeatz.gnarlybeatzServer.user.Role.ADMIN;
import static pl.gnarlybeatz.gnarlybeatzServer.user.Role.USER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors((c) -> c.configurationSource(corsConfig.configurationCorsSource()))
                .authorizeHttpRequests(request ->
                        request.requestMatchers(
                                        "/v3/api-docs/**", //todo raczej potem usunac
                                        "/swagger-ui/**",
                                        "/api/email/**",
                                        "/api/audio/search",
                                        "/api/audio/filter/values",
                                        "/api/auth/register",
                                        "/api/auth/authenticate",
                                        "/api/payments/stripe/events"
                                )
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/auth/refreshToken").hasAnyRole(ADMIN.name(), USER.name())
                                .requestMatchers(HttpMethod.POST, "/api/user/edit").hasAnyRole(ADMIN.name(), USER.name())
                                .requestMatchers(HttpMethod.POST, "/api/audio/archiveBeat").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/audio/uploadOrUpdate").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/audio/listOfAudioFilesToUpdate").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.GET, "/api/audio/listOfArchiveBeat").hasRole(ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/api/audio/favoriteBeats/add").hasAnyRole(ADMIN.name(), USER.name())
                                .requestMatchers(HttpMethod.POST, "/api/audio/favoriteBeats/remove").hasAnyRole(ADMIN.name(), USER.name())
                                .requestMatchers(HttpMethod.GET, "/api/audio/download").hasRole(USER.name())
                                .requestMatchers(HttpMethod.POST, "/api/payments/checkout").hasRole(USER.name())
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()));
        return http.build();
    }
}
