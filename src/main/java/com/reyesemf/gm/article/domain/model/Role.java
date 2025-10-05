package com.reyesemf.gm.article.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = Role.NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "role_name_uk",
                        columnNames = {"name"})
        }
)
public class Role extends DomainEntity {

    public static final String NAME = "role";

    @Serial
    private static final long serialVersionUID = 283746198273649182L;

    @Column(name = "name", nullable = false, length = 32)
    @Enumerated(STRING)
    private RoleName name;

    @Column(name = "description", nullable = false, length = 128)
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = LAZY)
    private List<User> users;

    @ManyToMany(fetch = LAZY)
    @JoinTable(
            name = "role_action",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id")
    )
    private List<Action> actions;

    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

}
