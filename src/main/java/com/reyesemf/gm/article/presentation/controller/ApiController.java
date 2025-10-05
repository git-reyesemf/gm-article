package com.reyesemf.gm.article.presentation.controller;

import com.reyesemf.gm.article.domain.model.Article;
import com.reyesemf.gm.article.domain.model.Category;
import com.reyesemf.gm.article.domain.model.Session;
import com.reyesemf.gm.article.domain.model.User;
import com.reyesemf.gm.article.domain.service.ArticleService;
import com.reyesemf.gm.article.domain.service.AuthenticationService;
import com.reyesemf.gm.article.domain.service.CategoryService;
import com.reyesemf.gm.article.infrastructure.RequiredAction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.reyesemf.gm.article.domain.model.ActionName.*;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api")
@Tag(name = "API", description = "API principal para gestión de categorías y artículos")
public class ApiController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/category")
    @RequiredAction(GET_ALL_CATEGORIES)
    @Operation(summary = "Obtener todas las categorías", description = "Devuelve una lista con todas las categorías disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    })
    public ResponseEntity<List<Category>> getAllCategories() {
        return ok(categoryService.getAll());
    }

    @GetMapping("/category/{category_slug}")
    @RequiredAction(GET_CATEGORY)
    @Operation(summary = "Obtener categoría", description = "Devuelve categpría por slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente")
    })
    public ResponseEntity<Category> getCategory(@PathVariable("category_slug") String categorySlug) {
        return ok(categoryService.getBySlug(categorySlug));
    }

    @GetMapping("/category/{category_slug}/articles")
    @RequiredAction(GET_ALL_ARTICLES_BY_CATEGORY)
    @Operation(summary = "Obtener artículos por categoría", description = "Devuelve todos los artículos de una categoría específica usando su slug")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de artículos obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    public ResponseEntity<List<Article>> getAllArticlesByCategory(
            @Parameter(description = "Slug de la categoría", required = true)
            @PathVariable("category_slug") String categorySlug) {
        return ok(articleService.getAllByCategorySlug(categorySlug));
    }

    @GetMapping("/article/{article_slug}")
    @RequiredAction(GET_ARTICLE)
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

    /**
     * Autentica un usuario y retorna una sesión activa.
     * 
     * <p><strong>Campos requeridos en el objeto User:</strong></p>
     * <ul>
     *   <li><strong>username</strong>: Nombre de usuario único</li>
     *   <li><strong>password</strong>: Contraseña hasheada con algoritmo (64 caracteres hexadecimales)</li>
     * </ul>
     * 
     * <p><strong>Formato del password:</strong></p>
     * <p>El password debe ser un hash válido, compuesto por exactamente 64 caracteres
     * hexadecimales (0-9, a-f, A-F). Ejemplo:</p>
     * <pre>
     * "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
     * </pre>
     * 
     * <p><strong>Nota:</strong> Otros campos del objeto User (como email, roles, etc.) 
     * son opcionales para la autenticación y serán ignorados.</p>
     *
     * @param credentials Objeto User con username y password requeridos
     * @return ResponseEntity con la sesión creada si la autenticación es exitosa
     * @throws IllegalArgumentException si el password no tiene formato válido
     * @throws org.springframework.security.web.authentication.session.SessionAuthenticationException si las credenciales son inválidas
     */
    @PostMapping("/authentication")
    @RequiredAction(LOGIN)
    @Operation(
        summary = "Autenticación de usuario", 
        description = "Autentica un usuario con username y password y retorna una sesión activa"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa - Retorna sesión activa"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos - Password no tiene formato correcto"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas - Username o password incorrectos"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - Usuario sin permisos de LOGIN")
    })
    public ResponseEntity<Session> authenticate(
            @Parameter(
                description = "Credenciales del usuario. Requiere 'username' y 'password' hasheado",
                required = true,
                example = """
                    {
                        "username": "admin_user",
                        "password": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
                    }
                    """
            )
            @RequestBody User credentials) {
        return ok(authenticationService.authenticate(credentials));
    }

}