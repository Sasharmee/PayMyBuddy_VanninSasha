package com.openclassrooms.paymybuddy.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Classe de la configuration de la sécurité de Pay My Buddy.
 * <p>
 * Définit les régles d'accès aux différentes routes, la configuration de l'authentification et le logout.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Définit le mécanisme de chiffrement des mots de passe.
     * <p>
     * Utilise Bcrypt pour assurer la sécurité des mots de passe stockés.
     *
     * @return un encodeur de mot de passe
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuration de la chaine de filtres de la sécurité HTTP.
     <p>
     * Définit :
     * <ul>
     *     <li>Les routes accessibles sans authentification</li>
     *     <li>Les routes nécessitant une authentification</li>
     *     <li>La configuration du formulaire de connexion</li>
     *     <li>La gestion de la déconnexion</li>
     * </ul>
     *
     * @param http objet permettant de configurer la sécurité HTTP
     * @return la configuration de la chaine de filtres HTTP
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/robots.txt",
                                "/api/auth/**",
                                "/login",
                                "/register",
                                "/css/**",
                                "/js/**",
                                "/images/**"

                        ).permitAll()
                        .requestMatchers("/profile", "/transfer", "/friends").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/profile", false)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                ;

        return http.build();
    }
}
