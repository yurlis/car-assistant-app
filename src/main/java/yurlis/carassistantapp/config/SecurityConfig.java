package yurlis.carassistantapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Використання нового способу активації CORS
                .csrf(csrf -> csrf.disable()) // Вимкнення CSRF для спрощення
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/public/**", "/auth/**", "/swagger-ui/**", "/v3/api-docs/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                //.httpBasic(withDefaults()) swagger with this line don`t works
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .userDetailsService(userDetailsService)
                .build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration
//    ) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Дозволити запити з будь-якого джерела
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Дозволені методи
        configuration.setAllowedHeaders(List.of("*")); // Дозволити будь-які заголовки
        configuration.setAllowCredentials(false); // Заборонити передачу cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Застосувати CORS до всіх шляхів
        return source;
    }
}
