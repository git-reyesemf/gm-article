package com.reyesemf.gm.article.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.time.LocalDateTime;

import static com.reyesemf.gm.article.domain.model.Session.Status.ACTIVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static java.time.LocalDateTime.now;

@Entity
@Table(name = Session.NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "session_token_uk",
                        columnNames = {"token"})
        }
)
public class Session extends DomainEntity {

    public enum Status {
        ACTIVE,
        CLOSED
    }

    public static final String NAME = "session";

    @Serial
    private static final long serialVersionUID = 384756291847365928L;

    @Column(name = "token", nullable = false, length = 64, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Enumerated(STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isAvailable() {
        return ACTIVE.equals(status) && now().isBefore(expiresAt);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}