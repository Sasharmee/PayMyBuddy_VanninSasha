package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    //Test GET: Happy path login
    @Test
    void login_shouldReturnLoginPage() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    //Test GET: Happy path register
    @Test
    void register_shouldReturnRegisterPage() throws Exception{

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    //Test POST: happy path register - inscription réussie
    @Test
    void registerUser_shouldRegisterAndReturnToLoginPage() throws Exception{

        mockMvc.perform(post("/register")
                .param("username", "marco")
                .param("email", "marco@mail.com")
                .param("password", "7272"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).registerUser(
                "marco",
                "marco@mail.com",
                "7272"
        );
    }

    //Test POST : erreur lors de l'inscription
    @Test
    void registerUser_whenError_shouldReturnErrorPage() throws Exception{

        doThrow(new IllegalArgumentException("Email already used"))
                .when(userService).registerUser(any(), any(), any());

        mockMvc.perform(post("/register")
                        .with(csrf())
                .param("username", "marco")
                .param("email", "marco@mail.com")
                .param("password", "7272"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"));

        verify(userService).registerUser(
                "marco",
                "marco@mail.com",
                "7272"
        );
    }

}
