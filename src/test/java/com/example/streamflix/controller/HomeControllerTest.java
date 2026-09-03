package com.example.streamflix.controller;

import com.example.streamflix.dto.HeroContent;
import com.example.streamflix.entity.ContentType;
import com.example.streamflix.entity.Profile;
import com.example.streamflix.security.DatabaseUserDetailsService;
import com.example.streamflix.security.LoginSuccessHandler;
import com.example.streamflix.security.SecurityConfig;
import com.example.streamflix.service.CatalogService;
import com.example.streamflix.service.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = HomeController.class)
@Import(SecurityConfig.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CatalogService catalogService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private DatabaseUserDetailsService databaseUserDetailsService;

    @MockitoBean
    private LoginSuccessHandler loginSuccessHandler;

    @Test
    void homeRedirectsAnonymousUsersToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "user1@streamflix.local")
    void homeReturnsIndexWhenAuthenticated() throws Exception {
        when(currentUserService.requireProfile(any())).thenReturn(new Profile());
        when(catalogService.featuredHero()).thenReturn(new HeroContent(
                1L, "Night Circuit", "Desc", 2024, "PG-13", 118, "x", ContentType.MOVIE, "/movies/1", "/watch/movie/1"));
        when(catalogService.homeRows(any())).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"));
    }
}
