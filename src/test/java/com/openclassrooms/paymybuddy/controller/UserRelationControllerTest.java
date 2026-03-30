package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.service.UserRelationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires du contrôleur UserRelationController.
 * <p>
 * Vérifie la récupération des amis ainsi que l'ajout de nouvelles relations.
 * Utilise MockMvc pour simuler les requêtes HTTP et Mockito pour simuler le service.
 */
@WebMvcTest(UserRelationController.class)
@WithMockUser(username = "user@mail.com")
public class UserRelationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRelationService userRelationService;

    /**
     * Vérifie que la liste des amis est correctement retournée.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void getFriends_shouldReturnFriends() throws Exception{
        User friend = new User("marco", "marco@mail.com", "7272");

        when(userRelationService.getFriends("user@mail.com"))
                .thenReturn(List.of(friend));

        mockMvc.perform(get("/friends").principal(()->"user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("friends"))
                .andExpect(model().attributeExists("friends"));

        verify(userRelationService).getFriends("user@mail.com");
    }

    /**
     * Vérifie que la relation est correctement ajoutée.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void addFriend_shouldAddFriends() throws Exception{

        mockMvc.perform(post("/add_friends")
                        .with(csrf())
                    .param("friendEmail", "friend@mail.com")
                    .principal(()->"user@mail.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/friends"));

        verify(userRelationService).addFriend("user@mail.com", "friend@mail.com");
    }

    /**
     * Vérifie que la page erreur est affiché lors que les utilisateurs sont déjà amis.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void addFriend_whenUsersAreAlreadyFriends_shouldReturnErrorPage() throws Exception {

        doThrow(new IllegalArgumentException("Already friend"))
                .when(userRelationService).addFriend("user@mail.com", "friend@mail.com");

        mockMvc.perform(post("/add_friends")
                        .with(csrf())
                    .param("friendEmail", "friend@mail.com")
                    .principal(()->"user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"));

        verify(userRelationService).addFriend("user@mail.com", "friend@mail.com");
    }

}
