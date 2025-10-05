package com.reyesemf.gm.article.domain.service;

import com.reyesemf.gm.article.domain.model.Session;
import com.reyesemf.gm.article.domain.model.User;
import com.reyesemf.gm.article.infrastructure.RequiredAction;

public interface AuthenticationService {
    
    /**
     * Autentica un usuario y crea una nueva sesión activa.
     * 
     * <p><strong>Campos requeridos del objeto User:</strong></p>
     * <ul>
     *   <li><strong>username</strong>: Nombre de usuario único registrado en el sistema</li>
     *   <li><strong>password</strong>: Contraseña hasheada</li>
     * </ul>
     * 
     * <p><strong>Formato del password:</strong></p>
     * <p>El password debe ser un hash válido de exactamente 64 caracteres
     * hexadecimales (0-9, a-f, A-F).</p>
     * 
     * <p><strong>Comportamiento:</strong></p>
     * <ul>
     *   <li>Crea una nueva sesión con estado ACTIVE</li>
     *   <li>Establece tiempo de expiración (por defecto 1 hora)</li>
     *   <li>Genera un token único para la sesión</li>
     * </ul>
     *
     * @param user Objeto User con username y password requeridos
     * @return Session nueva sesión activa con token generado
     * @throws IllegalArgumentException si el User es null o los datos son inválidos
     * @throws org.springframework.dao.DataIntegrityViolationException si hay problemas de persistencia
     */
    Session authenticate(User user);
    
    /**
     * Valida un token de sesión y verifica permisos para una acción específica.
     *
     * @param sessionToken Token de la sesión a validar
     * @param required Acción requerida que debe estar permitida para el usuario
     * @return Session sesión validada con tiempo de expiración extendido
     * @throws org.springframework.security.web.authentication.session.SessionAuthenticationException si el token es inválido
     * @throws org.springframework.security.authentication.CredentialsExpiredException si la sesión ha expirado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no tiene permisos
     */
    Session validate(String sessionToken, RequiredAction required);
}
