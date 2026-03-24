package spotfit.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth

            	    // ── Preflight CORS ──
            	    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            	    // ── Rutas públicas ──
            	    .requestMatchers("/", "/login", "/error").permitAll()

            	    // ── Noticias: lectura autenticada, gestión solo ADMIN ──
            	    .requestMatchers(HttpMethod.GET, "/noticias", "/noticias/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MONITOR", "ROLE_CLIENTE")
            	    .requestMatchers("/noticias", "/noticias/**").hasAuthority("ROLE_ADMIN")

            	    // ── Videos: lectura autenticada, gestión solo ADMIN ──
            	    .requestMatchers(HttpMethod.GET, "/videos", "/videos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MONITOR", "ROLE_CLIENTE")
            	    .requestMatchers("/videos", "/videos/**").hasAuthority("ROLE_ADMIN")

            	    // ── Administración: solo ADMIN ──
            	    .requestMatchers("/usuarios", "/usuarios/**").hasAuthority("ROLE_ADMIN")
            	    .requestMatchers("/salas", "/salas/**").hasAuthority("ROLE_ADMIN")
            	    .requestMatchers("/servicios", "/servicios/**").hasAuthority("ROLE_ADMIN")

            	    // ── Sesiones: lectura para los 3 roles, gestión ADMIN ──
            	    .requestMatchers(HttpMethod.GET, "/sesiones", "/sesiones/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MONITOR", "ROLE_CLIENTE")
            	    .requestMatchers("/sesiones", "/sesiones/**").hasAuthority("ROLE_ADMIN")

            	    // ── Reservas: ADMIN + CLIENTE ──
            	    .requestMatchers("/reservas", "/reservas/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_CLIENTE")

            	    // ── Todo lo demás requiere autenticación ──
            	    .anyRequest().authenticated()
            	)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(
            "https://aitorserrano.com",
            "https://www.aitorserrano.com",
            "http://localhost:4200"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}