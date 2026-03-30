package com.openclassrooms.paymybuddy.security;

import com.openclassrooms.paymybuddy.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implémentation personnalisée de l'interface UserDetails de Spring Security.
 * <p>
 * Cette classe permet d'adapter l'entité User au système d'authentification de Spring Security.
 * Elle fournit les informations nécessaires à l'authentification
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    /**
     * Constructeur permettant d'encapsuler un utilisateur.
     *
     * @param user entité utilisateur adapté à Spring Security
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Retourne les rôles/autorités de l'utilisateur.
     * <p>
     * Aucun rôle spécifique définit.
     *
     * @return liste vide d'autorité
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return mot de passe chiffré
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Retourne le nom d'utilisateur utilisé pour l'authentification.
     * <p>
     * Le mail est utilisé comme identifiant.
     *
     * @return email de l'utilisateur
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indique si le compte n'est pas expiré.
     *
     * @return true signifiant que le compte est actif
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indique si le compte n'est pas verrouillé.
     *
     * @return true signifiant que le compte n'est pas verrouillé
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indique si les identifiants sont valides
     *
     * @return true signifiant que les identifiants sont bien valides.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indique si le compte est activé.
     *
     * @return true signifiant que le compte est bien actif
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
