package com.openclassrooms.paymybuddy.service;


import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires du service UserRelationService.
 * <p>
 * Vérifie la gestion des relations entre utilisateurs.
 * Utilise Mockito pour isoler le service et simuler les dépendances.
 */
@ExtendWith(MockitoExtension.class)
public class UserRelationServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserRelationService userRelationService;

    private User currentUser;
    private User friend;

    /**
     * Initialise les données de test.
     */
    @BeforeEach
    void setUp(){
        currentUser = new User("currentUser", "currentUser@mail.com", "1234");
        currentUser.setId(1);
        currentUser.setFriends(new ArrayList<>());
        friend = new User("friend", "friend@mail.com", "1111");
        friend.setId(2);
        friend.setFriends(new ArrayList<>());
    }

    /**
     * Vérifie que l'ajout de la relation a correctement lieu.
     */
    @Test
    void addFriend_whenUsersAreValid_savesRelation() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("friend@mail.com")).thenReturn(Optional.of(friend));
        userRelationService.addFriend("currentUser@mail.com", "friend@mail.com");

        assertTrue(currentUser.getFriends().contains(friend));
        verify(userRepository).save(currentUser);

    }

    /**
     * Vérifie qu'une exception est levée lorsqu'un utilisateur n'est pas trouvé.
     */
    @Test
    void addFriend_userNotFound_throwsException() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.empty()
        );
        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userRelationService.addFriend("currentUser@mail.com", "friend@mail.com"));

        assertEquals("User not found", exception.getMessage());
    }

    /**
     * Vérifie qu'une exception est levée lorsqu'un utilisateur souhaite s'ajouter lui-même.
     */
    @Test
    void addFriend_addHimself_throwsException() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userRelationService.addFriend("currentUser@mail.com", "currentUser@mail.com"));

        assertEquals("You cannot add yourself", exception.getMessage());
    }

    /**
     * Vérifie qu'une exception est levée lorsque les utilisateurs sont déjà amis.
     */
    @Test
    void addFriend_alreadyFriend_throwsException() {
        currentUser.getFriends().add(friend);

        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("friend@mail.com")).thenReturn(Optional.of(friend));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userRelationService.addFriend("currentUser@mail.com","friend@mail.com" ));

        assertEquals("Already friend", exception.getMessage());
    }

    /**
     * Vérifie que la liste des amis est correctement retournée.
     */
    @Test
    void getFriends_whenUserIsValid_returnsListOfFriends() {

        currentUser.getFriends().add(friend);

        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));

        var friends = userRelationService.getFriends("currentUser@mail.com");

        assertEquals(1, friends.size());
        assertTrue(friends.contains(friend));
    }

    /**
     * Vérifie qu'une exception est levée lorsque l'utilisateur n'est pas trouvé.
     */
    @Test
    void getFriend_userNotFound_throwsException() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class , ()->
                userRelationService.getFriends("currentUser@mail.com"));

    }

}
