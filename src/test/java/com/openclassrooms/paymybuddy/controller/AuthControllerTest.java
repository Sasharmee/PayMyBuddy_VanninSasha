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

/**
 * Tests unitaires du contrôleur AuthController.
 * <p>
 * Vérifie le bon fonctionnement des endpoints liés à l'authentification :
 * affichage des pages et gestion de l'inscription.
 * <p>
 * Utilisation de MockMvc afin de simuler les requêtes HTTP et mockito pour simuler le service.
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    /**
     * Vérification que la page connexion s'affiche correctement.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void login_shouldReturnLoginPage() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    /**
     * Vérifie que lorsqu'il y a une erreur dans les paramètres,
     * l'utilisateur est renvoyé vers une page login.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void login_whenErrorParameter_shouldDisplayError() throws Exception {

        mockMvc.perform(get("/login?error"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    /**
     * Vérifie que lorsqu'un utilisateur renseigne les bons paramètres login,
     * alors il est connecté (démonstration).
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void testLogin_whenValid_shouldReturnSuccess() throws Exception {

        mockMvc.perform(post("/test-login")
                        .param("email", "marco@mail.com")
                        .param("password", "7272"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

    }

    /**
     * Vérification que la page d'inscription s'affiche correctement.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
    @Test
    void register_shouldReturnRegisterPage() throws Exception{

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    /**
     * Vérifie que l'enregistrement est un succès
     * et que la redirection vers la page de connexion s'effectue correctement.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
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

    /**
     * Vérifie que lorsqu'il y a une erreur lors de l'enregistrement il y a la redirection vers la page erreur.
     *
     * @throws Exception en cas d'erreur lors de la requête
     */
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
