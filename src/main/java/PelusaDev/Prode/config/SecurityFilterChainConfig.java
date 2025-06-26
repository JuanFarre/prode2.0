package PelusaDev.Prode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Anotación que marca esta clase como una clase de configuración en Spring.
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

        httpSecurity.cors(); // habilita el uso de configuración global definida en WebMvcConfigurer
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(requestMatcher -> requestMatcher
                // Rutas públicas
                .requestMatchers("/api/auth/login/**").permitAll()
                .requestMatchers("/api/auth/registrar/**").permitAll()
                .requestMatchers("/api/auth/verifyEmail/**").permitAll()
                .requestMatchers("/api/usuarios/**").permitAll()
                .requestMatchers("/api/productos/**").permitAll()
                .requestMatchers("/api/uploads/**").permitAll()

                // Rutas públicas del controlador EquipoController
                .requestMatchers("/api/equipos").permitAll()          // GET all equipos
                .requestMatchers("/api/equipos/*").permitAll()        // GET equipo por ID

                // Rutas protegidas del controlador EquipoController
                .requestMatchers("/api/equipos/crear").hasRole("ADMIN")
                .requestMatchers("/api/equipos/editar/**").hasRole("ADMIN")
                .requestMatchers("/api/equipos/eliminar/**").hasRole("ADMIN")

                // Rutas de tickets: autenticado puede crear y ver sus tickets
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