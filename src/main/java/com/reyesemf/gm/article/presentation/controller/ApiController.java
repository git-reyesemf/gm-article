package com.reyesemf.gm.article.presentation.controller;

import com.reyesemf.gm.article.datasource.repository.ArticleRepository;
import com.reyesemf.gm.article.datasource.repository.CategoryRepository;
import com.reyesemf.gm.article.domain.model.Article;
import com.reyesemf.gm.article.domain.model.Category;
import com.reyesemf.gm.article.domain.service.ArticleService;
import com.reyesemf.gm.article.domain.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@Tag(name = "API", description = "API principal para gestión de categorías y artículos")
public class ApiController {

    @Autowired
    private ArticleService articleService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ArticleRepository articleRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/category")
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista con todas las categorías disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    })
    public ResponseEntity<List<Category>> getAllCategories() {
        return ok(categoryRepository.findAll());
    }

    @GetMapping("/category/{category_slug}/articles")
    @Operation(summary = "Obtener artículos por categoría", description = "Devuelve todos los artículos de una categoría específica usando su slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de artículos obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<List<Article>> getAllArticlesByCategory(
            @Parameter(description = "Slug de la categoría", required = true)
            @PathVariable("category_slug") String categorySlug) {
        return ok(articleRepository.findAllByCategorySlug(categorySlug));
    }

    @GetMapping("/article/{article_slug}")
    @Operation(summary = "Obtener artículo", description = "Devuelve todos los atributos de un artículo usando su slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artículo obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    public ResponseEntity<Article> getArticle(
            @Parameter(description = "Slug del artículo", required = true)
            @PathVariable("article_slug") String articleSlug) {
        return ok(articleService.getBySlug(articleSlug));
    }

}