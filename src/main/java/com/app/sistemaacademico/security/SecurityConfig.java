package com.app.sistemaacademico.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Le indica al contenedor de spring que esta es una clase de seguridad al momento de arrancar la aplicacion
@EnableWebSecurity // Indicamos que se activa la seguridad web en nuestra aplicacion y ademas esta sera una clase la cual contendra toda la configuracion referenate a la seguiridad
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    
    // Este bean va a encargarse de verificar la informacion de los usuarios que se loguearan en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    // Con este bean nos encargaremos de encriptar todas nuestras contrasenas
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    // Este bean incorporara el filtro de seguridad de jwt que creamos en nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }
    
    // Vamos a crear un bean que va a establecer una cadena de filtros de seguridad en nuestra aplicacion y 
    // es aqui donde determinaremos los permisos segun los roles de usuario para acceder a nuestra aplicacion
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/admin/**").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.POST, "/api/admin/**").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasAuthority("Admin")
                        .requestMatchers(HttpMethod.GET, "/api/professor/**").hasAuthority("Professor")
                        .requestMatchers(HttpMethod.POST, "/api/professor/**").hasAuthority("Professor")
                        .requestMatchers(HttpMethod.PUT, "/api/professor/**").hasAuthority("Professor")
                        .requestMatchers(HttpMethod.GET, "/api/student/**").hasAuthority("Student")
                        .requestMatchers(HttpMethod.POST, "/api/student/**").hasAuthority("Student")
                        .requestMatchers(HttpMethod.PUT, "/api/student/**").hasAuthority("Student")

                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> {});
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
           
    }

    
}
