package com.reyesemf.gm.article.configuration.security;

import com.reyesemf.gm.article.domain.model.Action;
import com.reyesemf.gm.article.domain.model.Session;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementación de Authentication de Spring Security basada en Session.
 * 
 * <p>Esta clase adapta el modelo de dominio {@link Session} al contrato
 * de Spring Security {@link Authentication}, permitiendo que la sesión
 * validada se integre completamente con el ecosistema de Spring Security.
 */
public class SessionAuthentication implements Authentication {

    private final Session session;
    private boolean authenticated = true;

    public SessionAuthentication(Session session) {
        this.session = session;
    }

    /**
     * Retorna las autoridades (permisos) del usuario.
     * Convierte los ActionName a GrantedAuthority de Spring Security.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return session.getUser().getRoles().stream()
                .flatMap(role -> role.getActions().stream())
                .map(Action::getName)
                .map(actionName -> new SimpleGrantedAuthority("ACTION_" + actionName.name()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna el token de la sesión como credencial.
     */
    @Override
    public Object getCredentials() {
        return session.getToken();
    }

    /**
     * Retorna información adicional (el objeto Session completo).
     */
    @Override
    public Object getDetails() {
        return session;
    }

    /**
     * Retorna el usuario autenticado.
     */
    @Override
    public Object getPrincipal() {
        return session.getUser();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated && session.isAvailable();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a Session");
        }
        this.authenticated = false;
    }

    /**
     * Retorna el username como nombre del principal.
     */
    @Override
    public String getName() {
        return session.getUser().getUsername();
    }

    /**
     * Obtiene la sesión subyacente.
     */
    public Session getSession() {
        return session;
    }
}
