package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserAccountManagementInterface{

    public final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }


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

        User newUser = new User(username, email, encodedPassword);

        return userRepository.save(newUser);
    }

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
