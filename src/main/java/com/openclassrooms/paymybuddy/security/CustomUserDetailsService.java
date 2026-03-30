package com.openclassrooms.paymybuddy.security;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service permettant de charger les informations d'un utilisateur pour l'authentification via Spring Security.
 *
 * Implémente l'interface UserDetailsService afin de fournir à Spring Security
 * les données nécessaires à l'identification d'un utilisateur à partir de son adresse email.
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructeur du service de gestion des utilisateurs pour la sécurité.
     *
     * @param userRepository repository permettant d'accéder aux utilisateurs en base de données
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charger un utilisateur via son identifiant (ici l'email).
     * <p>
     * Méthode appelée automatique par Spring Security lors du processus d'authentification.
     *
     * @param username email de l'utilisateur
     * @return un objet UserDetails contenant les informations de l'utilisateur
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
