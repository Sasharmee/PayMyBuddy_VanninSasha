package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service UserService.
 * <p>
 * Vérifie la logique métier liée aux utilisateurs.
 * Utilise Mockito pour isoler le service et simuler les dépendances.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    private User user;

    /**
     * Initialisation des données de test.
     */
    @BeforeEach
    void SetUp() {
        user = new User("marco", "marco@mail.com", "7272");
        user.setId(1);
    }

    /**
     * Vérifie qu'un utilisateur est correctement inscrit et enregistré.
     */
    @Test
    void registerUser_whenDataAreValid_savesUser() {
        when(userRepository.findByEmail("marco@mail.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("marco")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("7272")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.registerUser("marco" , "marco@mail.com", "7272");

        assertEquals("marco", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("7272");
    }

    /**
     * Vérifie qu'une exception est levée lorsque l'email est déjà utilisé.
     */
    @Test
    void registerUser_whenEmailIsAlreadyUsed_throwsException() {
        when(userRepository.findByEmail("marco@mail.com")).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userService.registerUser("marco", "marco@mail.com", "7272"));

        assertEquals("Email already used", exception.getMessage());

        verify(userRepository, never()).save(any());

    }

    /**
     * Vérifie qu'une exception est levée lorsque le nom d'utilisateur est déjà utilisé.
     */
    @Test
    void registerUser_whenUsernameIsAlreadyUsed_throwsException() {
        when(userRepository.findByEmail("marco@mail.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("marco")).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userService.registerUser("marco", "marco@mail.com", "7272"));

        assertEquals("Username already used", exception.getMessage());

        verify(userRepository, never()).save(any());

    }

    /**
     * Vérifie que la mise à jour est correctement effectuée.
     */
    @Test
    void updateUserInformations_whenDataAreValid_updatesUserInformations() {
        when(userRepository.findByEmail("marco@mail.com")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("marcoNew")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result =userService.updateUserInformations("marcoNew", "marco@mail.com", "newPassword");

        assertEquals("marcoNew", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).save(user);
        verify(passwordEncoder).encode("newPassword");
    }

    /**
     * Vérifie qu'une exception est levée lorsque l'utilisateur n'est pas retrouvé.
     */
    @Test
    void updateUserInformations_whenUserIsNotFound_throwsException() {
        when(userRepository.findByEmail("marco@mail.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->
                userService.updateUserInformations("marco", "marco@mail.com", "7272"));

        verify(userRepository, never()).save(any());
    }

    /**
     * Vérifie qu'une exception est levée lorsque le nom d'utilisateur est déjà utilisé.
     */
    @Test
    void updateUserInformations_usernameIsAlreadyUsedByAnotherUser_throwsException() {
        User anotherUser = new User("other", "other@mail.com", "pw123");
        anotherUser.setId(2);

        when(userRepository.findByEmail("marco@mail.com")).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("existingUsername")).thenReturn(Optional.of(anotherUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.updateUserInformations("existingUsername", "marco@mail.com", "7272"));

        assertEquals("Username already used", exception.getMessage());

        verify(userRepository, never()).save(any());
    }

}
