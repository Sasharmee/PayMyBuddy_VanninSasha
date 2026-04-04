package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Service gérant la logique métier liée aux utilisateurs.
 * <p>
 * Permet l'inscription, la modification des informations
 * ainsi que la récupération des utilisateurs via l'email ou le nom d'utilisateur
 *
 */
@Service
public class UserService implements UserAccountManagementInterface{

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur du service utilisateur.
     *
     * @param userRepository repository des utilisateurs
     * @param passwordEncoder encodeur utilisé pour chiffrer les mots de passe
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username nom d'utilisateur
     * @return un Optional contenant l'utilisateur s'il existe
     */
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email email de l'utilisateur
     * @return un Optional contenant l'utilisateur s'il existe
     */
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    /**
     * Enregistre un nouvel utilisateur.
     * <p>
     * Vérifie :
     * <ul>
     *     <li>Que l'email n'est pas déjà utilisé</li>
     *     <li>Que le nom d'utilisateur est unique</li>
     * </ul>
     * Le mot de passe est chiffré avant d'être enregistré.
     *
     * @param username nom d'utilisateur
     * @param email email de l'utilisateur
     * @param password mot de passe de l'utilisateur (chiffré par la suite)
     * @return l'utilisateur
     * @throws IllegalArgumentException en cas d'erreur
     */
    @Transactional
    @Override
    public User registerUser(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()){
            throw  new IllegalArgumentException("Email already used");
        }

        if (userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("Username already used");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User newUser = new User(username, email, encodedPassword, new BigDecimal("20"));

        return userRepository.save(newUser);
    }

    /**
     * Authentifie un utilisateur en vérifiant son email et son mot de passe.
     *
     * @param email email de l'utilisateur
     * @param rawPassword mot de passe saisi (non chiffré)
     * @return l'utilisateur authentifié
     * @throws IllegalArgumentException si les identifiants sont invalides
     */
    @Override
    public User login(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return user;
    }


    /**
     * Met à jour les informations d'un utilisateur existant.
     * <p>
     * Vérifie :
     * <ul>
     *     <li>Que l'utilisateur existe</li>
     *     <li>Que le nouveau nom d'utilisateur est disponible</li>
     * </ul>
     * Le mot de passe est re-chiffré avant mise à jour.
     *
     * @param username nom d'utilisateur
     * @param email email de l'utilisateur
     * @param password mot de passe de l'utilisateur (chiffré par la suite)
     * @return l'utilisateur mis à jour
     * @throws IllegalArgumentException en cas d'erreur
     */
    @Transactional
    @Override
    public User updateUserInformations(String username, String email, String password) {
        //Vérifier que l'utilisateur existe
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        // Vérifier si l'username est déjà utilisé
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Username already used");
        }

        // Mettre à jour les informations
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        // Sauvegarder
        return userRepository.save(user);
    }
}
