package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.entity.User;
import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser(username = "user@mail.com")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    //Test GET: Happy path récupération profil
    @Test
    void profile_shouldReturnProfilePage() throws Exception{

        User user = new User("user", "user@mail.com", "7272");

        when(userService.findByEmail("user@mail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/profile")
                        .principal(()->"user@mail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"));

        verify(userService).findByEmail("user@mail.com");
    }

    //Test GET: utilisateur non retrouvé
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

    //Test POST: happy path - mise à jour du profil réussie
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

    //Test POST: lorsque le nouveau username est déjà utilisé -> error page
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
