package com.reyesemf.gm.article.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = Action.NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "action_name_uk",
                        columnNames = {"name"})
        }
)
public class Action extends DomainEntity {

    public static final String NAME = "action";

    @Serial
    private static final long serialVersionUID = 192837465019283746L;

    @Column(name = "name", nullable = false, length = 64)
    @Enumerated(STRING)
    private ActionName name;

    @Column(name = "target", nullable = false, length = 256)
    private String target;

    @Column(name = "description", nullable = false, length = 128)
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "actions", fetch = LAZY)
    private List<Role> roles;

    public ActionName getName() {
        return name;
    }

    public void setName(ActionName name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
