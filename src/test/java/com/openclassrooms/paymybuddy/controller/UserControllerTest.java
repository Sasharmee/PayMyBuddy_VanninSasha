package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires du contrôleur UserController.
 * <p>
 * Vérifie l'affichage du profil utilisateur ainsi que la mise à jour du profil.
 * Utilise MockMvc pour simuler les requêtes HTTP et Mockito pour simuler le service.
 */
@WebMvcTest(UserController.class)
@WithMockUser(username = "user@mail.com")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    /**
     * Vérifie que la page profil est correctement affichée.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void profile_shouldReturnProfilePage() throws Exception{

        User user = new User("user", "user@mail.com", "7272", new BigDecimal("20"));

        when(userService.findByEmail("user@mail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/profile")
                        .principal(()->"user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"));

        verify(userService).findByEmail("user@mail.com");
    }

    /**
     * Vérifie que la page d'erreur est affichée lorsque l'utilisateur n'est pas retrouvé.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void profile_whenUserIsNotFound_shouldReturnErrorPage() throws Exception{

        when(userService.findByEmail("user@mail.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/profile")
                        .principal(() -> "user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"));

        verify(userService).findByEmail("user@mail.com");
    }

    /**
     * Vérifie que la mise à jour a correctement lieu et que l'utilisateur est redirigé vers la page profil.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void updateProfile_shouldUpdateProfileAndRedirectToProfilePage() throws Exception {

        mockMvc.perform(post("/update_profile")
                        .with(csrf())
                        .param("username", "newName")
                        .param("password", "newPassword")
                        .principal(() -> "user@mail.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        verify(userService).updateUserInformations(
                "newName",
                "user@mail.com",
                "newPassword"
        );
    }

    /**
     * Vérifie que la page erreur est affichée
     * lorsque l'utilisateur souhaite modifier son nom d'utilisateur par un étant déjà utilisé.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void updateProfile_whenUsernameIsAlreadyUsed_shouldReturnErrorPage() throws Exception{

        doThrow(new IllegalArgumentException("Username already used"))
                .when(userService)
                .updateUserInformations(any(), any(), any());

        mockMvc.perform(post("/update_profile")
                        .with(csrf())
                .param("username", "existingUser")
                .param("password","1234")
                .principal(()->"user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"));

        verify(userService).updateUserInformations( "existingUser",
                "user@mail.com",
                "1234");
    }
}
