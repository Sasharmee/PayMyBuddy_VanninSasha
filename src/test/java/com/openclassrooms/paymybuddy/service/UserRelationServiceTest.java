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

@ExtendWith(MockitoExtension.class)
public class UserRelationServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserRelationService userRelationService;

    private User currentUser;
    private User friend;

    @BeforeEach
    void setUp(){
        currentUser = new User("currentUser", "currentUser@mail.com", "1234");
        currentUser.setId(1);
        currentUser.setFriends(new ArrayList<>());
        friend = new User("friend", "friend@mail.com", "1111");
        friend.setId(2);
        friend.setFriends(new ArrayList<>());
    }

    //Happy Path : cas normal ou les deux utilisateurs existent et la relation est sauvegardée
    @Test
    void addFriend_whenUsersAreValid_savesRelation() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("friend@mail.com")).thenReturn(Optional.of(friend));
        userRelationService.addFriend("currentUser@mail.com", "friend@mail.com");

        assertTrue(currentUser.getFriends().contains(friend));
        verify(userRepository).save(currentUser);

    }

    //Test lorsqu'un des deux utilisateurs est introuvable
    @Test
    void addFriend_userNotFound_throwsException() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.empty()
        );
        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userRelationService.addFriend("currentUser@mail.com", "friend@mail.com"));

        assertEquals("User not found", exception.getMessage());
    }

    //Test lorsque l'utilisateur essaie de s'ajouter lui-même
    @Test
    void addFriend_addHimself_throwsException() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userRelationService.addFriend("currentUser@mail.com", "currentUser@mail.com"));

        assertEquals("You cannot add yourself", exception.getMessage());
    }

    @Test
    void addFriend_alreadyFriend_throwsException() {
        currentUser.getFriends().add(friend);

        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));
        when(userRepository.findByEmail("friend@mail.com")).thenReturn(Optional.of(friend));

        Exception exception = assertThrows(IllegalArgumentException.class, ()->
                userRelationService.addFriend("currentUser@mail.com","friend@mail.com" ));

        assertEquals("Already friend", exception.getMessage());
    }

    //Test happy path getFriends
    @Test
    void getFriends_whenUserIsValid_returnsListOfFriends() {

        currentUser.getFriends().add(friend);

        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.of(currentUser));

        var friends = userRelationService.getFriends("currentUser@mail.com");

        assertEquals(1, friends.size());
        assertTrue(friends.contains(friend));
    }

    //Test lorsqu'on ne retrouve pas l'utilisateur dont on cherche les amis
    @Test
    void getFriend_userNotFound_throwsException() {
        when(userRepository.findByEmail("currentUser@mail.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class , ()->
                userRelationService.getFriends("currentUser@mail.com"));

    }

}
