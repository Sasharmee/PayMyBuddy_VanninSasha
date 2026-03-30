package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service gérant les relations entre utilisateurs.
 * <p>
 * Permet de récupérer la liste d'amis d'un utilisateur ainsi qu'ajouter de nouvelles relations.
 *
 */
@Service
public class UserRelationService implements UserRelationInterface{

    private final UserRepository userRepository;

    /**
     * Constructeur du service de relations d'utilisateur.
     *
     * @param userRepository repository accédant aux utilisateurs
     */
    public UserRelationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Récupère la liste d'amis d'un utilisateur
     *
     * @param userEmail email de l'utilisateur
     * @return liste d'amis de l'utilisateur
     */
    @Override
    public List<User> getFriends(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(()->new IllegalArgumentException("User not found"));
        return user.getFriends();
    }

    /**
     * Ajoute un nouvel ami à un utilisateur.
     * <p>
     * Vérifie plusieurs règles métier :
     * <ul>
     *     <li>les utilisateurs existent</li>
     *     <li>l'utilisateur ne peut pas s'ajouter lui-même</li>
     *     <li>la relation n'existe pas déjà</li>
     * </ul>
     *
     * @param userEmail email de l'utilisateur
     * @param friendEmail email de l'ami
     * @throws IllegalArgumentException en cas d'erreur
     */
    @Override
    public void addFriend(String userEmail, String friendEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()->new IllegalArgumentException("User not found"));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(()->new IllegalArgumentException("Friend not found"));

        if(user.getId().equals(friend.getId())){
            throw new IllegalArgumentException("You cannot add yourself");}

        if (user.getFriends().stream()
                .anyMatch(f -> f.getId().equals(friend.getId()))){
            throw new IllegalArgumentException("Already friend");}

        user.getFriends().add(friend);

        userRepository.save(user);

    }
}
