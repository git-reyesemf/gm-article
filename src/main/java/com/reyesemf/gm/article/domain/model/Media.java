package com.reyesemf.gm.article.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.List;

@Entity
@Table(name = Media.NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                name = "media_name_uk",
                columnNames = {"name"}),
                @UniqueConstraint(
                name = "media_slug_uk",
                columnNames = {"slug"})
        }
)
public class Media extends DomainEntity {

    public static final String NAME = "media";

    @Serial
    private static final long serialVersionUID = 851372619485730126L;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Column(name = "slug", nullable = false, length = 32)
    private String slug;

    @Column(name = "description", nullable = false, length = 128)
    private String description;

    @Column(name = "image", nullable = false, length = 256)
    private String image;

    @Column(name = "url", nullable = false, length = 256)
    private String url;

    @ManyToMany(mappedBy = "relatedMedia")
    @JsonIgnore
    private List<Article> articles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

}
