package com.reyesemf.gm.article.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reyesemf.gm.article.infrastructure.RequiredAction;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = User.NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_username_uk",
                        columnNames = {"username"}),
                @UniqueConstraint(
                        name = "user_email_uk",
                        columnNames = {"email"})
        }
)
public class User extends DomainEntity {

    public static final String NAME = "app_user";

    @Serial
    private static final long serialVersionUID = 782349812739487123L;

    @Column(name = "username", nullable = false, length = 32)
    private String username;

    @Column(name = "email", nullable = false, length = 64)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = LAZY)
    private List<Session> sessions;

    @JsonProperty("roles")
    public List<Role> getRoles() {
        return roles;
    }

    public boolean hasAction(RequiredAction requiredAction) {
        return roles.stream()
                .map(Role::getActions)
                .flatMap(List::stream)
                .map(Action::getName)
                .anyMatch(requiredAction.value()::equals);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

}
