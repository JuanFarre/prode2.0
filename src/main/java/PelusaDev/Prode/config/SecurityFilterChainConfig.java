package PelusaDev.Prode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // Habilita las anotaciones de seguridad a nivel de método como @PreAuthorize
public class SecurityFilterChainConfig {

    private final AuthenticationEntryPoint authenticationEntryPoint; // Punto de entrada para manejar excepciones de autenticación.
    private final JWTAuthenticationFilter jwtAuthenticationFilter; // Filtro personalizado para manejar la autenticación con JWT.

    // Constructor de la clase que inicializa el `authenticationEntryPoint`.
    public SecurityFilterChainConfig(AuthenticationEntryPoint authenticationEntryPoint, JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Definición de un bean que configura la seguridad para las solicitudes HTTP.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Configurar CORS y CSRF
        httpSecurity.cors(cors -> cors.configure(httpSecurity)); // Configuración actualizada para Spring Security 6.1+
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(requestMatcher -> requestMatcher
                // ----- Autenticación -----
                // Rutas públicas para autenticación y registro
                .requestMatchers("/api/auth/login/**").permitAll()
                .requestMatchers("/api/auth/registrar/**").permitAll()
                .requestMatchers("/api/auth/verifyEmail/**").permitAll()
                
                // ----- Uploads y archivos estáticos -----
                .requestMatchers("/api/uploads/**").permitAll()
                
                // ----- Equipos -----
                // Lectura pública de equipos
                .requestMatchers(HttpMethod.GET, "/api/equipos/**").permitAll()
                
                // ----- Fechas -----
                // Lectura pública de fechas
                .requestMatchers(HttpMethod.GET, "/api/fechas/**").permitAll()
                
                // ----- Partidos -----
                // Lectura pública de partidos
                .requestMatchers(HttpMethod.GET, "/api/partidos/**").permitAll()
                
                // ----- Torneos -----
                // Lectura pública de torneos
                .requestMatchers(HttpMethod.GET, "/api/torneos/**").permitAll()
                
                // ----- Usuarios -----
                // Lectura pública de tablas generales
                .requestMatchers(HttpMethod.GET, "/api/usuarios/tabla-general/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/usuarios/tabla-por-fecha/**").permitAll()
                // Otras operaciones de usuarios requieren autenticación y se controlan con @PreAuthorize
                .requestMatchers("/api/usuarios/**").authenticated()
                
                // ----- Pronósticos -----
                // Requiere autenticación
                .requestMatchers("/api/pronosticos/**").authenticated()
                
                // ----- Tickets -----
                // Requiere autenticación
                .requestMatchers("/api/tickets/**").authenticated()
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
        );

        httpSecurity.exceptionHandling(
                exceptionConfig -> exceptionConfig.authenticationEntryPoint(authenticationEntryPoint)
        );

        httpSecurity.sessionManagement(
                sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}