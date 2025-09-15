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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        // Configurar MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("GET /api/category - Debería retornar todas las categorías con status 200")
    void getAllCategories_ShouldReturnAllCategoriesWithStatus200() throws Exception {
        mockMvc.perform(get("/api/category")
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
    void getAllArticlesByCategory_ShouldReturnArticlesForCategoryWithStatus200() throws Exception {
        mockMvc.perform(get("/api/category/{categorySlug}/articles", "automotores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$[0].name", is("Mobil 1 Advanced Full Synthetic 5W-30")))
                .andExpect(jsonPath("$[0].slug", is("mobil-1-advanced-5w30")))
                .andExpect(jsonPath("$[0].description", containsString("Aceite sintético premium")))
                .andExpect(jsonPath("$[1].name", is("Castrol GTX High Mileage 20W-50")))
                .andExpect(jsonPath("$[1].slug", is("castrol-gtx-20w50")));
    }

    @Test
    @DisplayName("GET /api/article/{article_slug} - Debería retornar el artículo específico con status 200")
    void getArticle_ShouldReturnSpecificArticleWithStatus200() throws Exception {
        mockMvc.perform(get("/api/article/{articleSlug}", "mobil-1-advanced-5w30")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Mobil 1 Advanced Full Synthetic 5W-30")))
                .andExpect(jsonPath("$.slug", is("mobil-1-advanced-5w30")))
                .andExpect(jsonPath("$.description", containsString("Aceite sintético premium")))
                .andExpect(jsonPath("$.url", is("/articulo/mobil-1-advanced-5w30")));
    }
}
