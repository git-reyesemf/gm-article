package com.reyesemf.gm.article.presentation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.reyesemf.gm.article.domain.model.Session.Status.ACTIVE;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("local")
@Sql(scripts = "classpath:/db/mysql/schema.sql")
@Sql(scripts = "classpath:/db/mysql/data.sql")
@DisplayName("ApiController Integration Tests")
class ApiControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("GET /api/category - Debería retornar todas las categorías con status 200")
    void givenValidRequestWhenGetAllCategoriesThenReturnsAllCategoriesWithStatus200() throws Exception {
        mockMvc.perform(get("/api/category")
                        .header("X-Auth-Token", "admin_user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name", is("Automotores")))
                .andExpect(jsonPath("$[0].slug", is("automotores")))
                .andExpect(jsonPath("$[0].description", is("Repuestos, accesorios y productos para vehículos")))
                .andExpect(jsonPath("$[1].name", is("Hogar")))
                .andExpect(jsonPath("$[1].slug", is("hogar")))
                .andExpect(jsonPath("$[2].name", is("Electrónica")))
                .andExpect(jsonPath("$[2].slug", is("electronica")))
                .andExpect(jsonPath("$[3].name", is("Camping")))
                .andExpect(jsonPath("$[3].slug", is("camping")));
    }

    @Test
    @DisplayName("GET /api/category/{category_slug}/articles - Debería retornar artículos de la categoría con status 200")
    void givenValidCategorySlugWhenGetAllArticlesByCategoryThenReturnsArticlesWithoutRelatedMediaAndStatus200() throws Exception {
        mockMvc.perform(get("/api/category/{categorySlug}/articles", "automotores")
                        .header("X-Auth-Token", "consumer_user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$[0].name", is("Mobil 1 Advanced Full Synthetic 5W-30")))
                .andExpect(jsonPath("$[0].slug", is("mobil-1-advanced-5w30")))
                .andExpect(jsonPath("$[0].description", containsString("Aceite sintético premium")))
                .andExpect(jsonPath("$[0].related_media").doesNotExist())
                .andExpect(jsonPath("$[1].name", is("Castrol GTX High Mileage 20W-50")))
                .andExpect(jsonPath("$[1].slug", is("castrol-gtx-20w50")))
                .andExpect(jsonPath("$[*].related_media").doesNotExist())
                .andExpect(jsonPath("$[1].related_media").doesNotExist())
                .andExpect(jsonPath("$[2].related_media").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/article/{article_slug} - Debería retornar el artículo específico con status 200")
    void givenValidArticleSlugWhenGetArticleThenReturnsArticleWithRelatedMediaAndStatus200() throws Exception {
        mockMvc.perform(get("/api/article/{articleSlug}", "mobil-1-advanced-5w30")
                        .header("X-Auth-Token", "admin_user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> System.out.println("JSON Response: " + result.getResponse().getContentAsString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Mobil 1 Advanced Full Synthetic 5W-30")))
                .andExpect(jsonPath("$.slug", is("mobil-1-advanced-5w30")))
                .andExpect(jsonPath("$.description", containsString("Aceite sintético premium")))
                .andExpect(jsonPath("$.url", is("/articulo/mobil-1-advanced-5w30")))
                .andExpect(jsonPath("$.related_media", hasSize(3)))
                .andExpect(jsonPath("$.related_media[0].name", is("Tutorial Mantenimiento Automotor")))
                .andExpect(jsonPath("$.related_media[1].name", is("Aceite Mobil 1 Vista Lateral")))
                .andExpect(jsonPath("$.related_media[2].name", is("Aceite Mobil 1 Etiqueta")));
    }

    @Test
    @DisplayName("POST /api/authentication - Debería autenticar usuario y retornar sesión con status 200")
    void givenValidUserCredentialsWhenAuthenticateThenReturnsSessionWithStatus200() throws Exception {
        String userJson = """
            {
                "username": "admin_user",
                "password": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
            }
            """;

        mockMvc.perform(post("/api/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.status", is(ACTIVE.name())))
                .andExpect(jsonPath("$.expires_at").exists());
    }

    @Test
    @DisplayName("POST /api/authentication - Debería fallar con usuario inexistente y retornar status 401")
    void givenNonExistentUserWhenAuthenticateThenReturnsStatus401() throws Exception {
        String invalidUserJson = """
            {
                "username": "nonexistent_user",
                "password": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
            }
            """;

        mockMvc.perform(post("/api/authentication")
                        .header("X-Auth-Token", "admin_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /api/authentication - Debería fallar con password inválido y retornar status 400")
    void givenInvalidPasswordFormatWhenAuthenticateThenReturnsStatus400() throws Exception {
        String invalidPasswordJson = """
            {
                "username": "admin_user",
                "password": "invalid-password-format"
            }
            """;

        mockMvc.perform(post("/api/authentication")
                        .header("X-Auth-Token", "admin_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPasswordJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/authentication - Debería fallar sin token de autorización y retornar status 401")
    void givenNoAuthTokenWhenAuthenticateThenReturnsStatus401() throws Exception {
        String userJson = """
            {
                "username": "testuser",
                "email": "test@example.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/authentication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }
}
