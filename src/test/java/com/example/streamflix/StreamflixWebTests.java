package com.example.streamflix;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class StreamflixWebTests {

    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("streamflix")
            .withUsername("streamflix")
            .withPassword("streamflix");

    @DynamicPropertySource
    static void registerDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginPageAndRegisterPageArePublic() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("auth/login"));
        mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("auth/register"));
    }

    @Test
    void registerCreatesAccountThenLoginSucceeds() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("firstName", "Sam")
                        .param("lastName", "Lee")
                        .param("email", "sam.lee@example.com")
                        .param("password", "streamflix"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(formLogin("/login").user("sam.lee@example.com").password("streamflix"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void seededUserCanOpenCatalogPages() throws Exception {
        var user = user("user1@streamflix.local").roles("USER");
        mockMvc.perform(get("/").with(user)).andExpect(status().isOk());
        mockMvc.perform(get("/movies").with(user)).andExpect(status().isOk());
        mockMvc.perform(get("/movies/1").with(user)).andExpect(status().isOk());
        mockMvc.perform(get("/tv").with(user)).andExpect(status().isOk());
        mockMvc.perform(get("/tv/1").with(user)).andExpect(status().isOk());
        mockMvc.perform(get("/search").param("q", "Night").with(user)).andExpect(status().isOk());
        mockMvc.perform(get("/my-list").with(user)).andExpect(status().isOk());
        mockMvc.perform(get("/watch/movie/1").with(user))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("archive.org")));
        mockMvc.perform(get("/profile").with(user)).andExpect(status().isOk());
    }

    @Test
    void myListApiAddsMovieWithoutDuplicateError() throws Exception {
        var user = user("user2@streamflix.local").roles("USER");
        mockMvc.perform(post("/api/my-list/movie/2").with(user).with(csrf()))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/my-list/movie/2").with(user).with(csrf()))
                .andExpect(status().isOk());
    }
}
