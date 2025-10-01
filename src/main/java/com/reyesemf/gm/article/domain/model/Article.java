package com.reyesemf.gm.article.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serial;
import java.util.List;
import java.util.function.Supplier;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = Article.NAME,
        uniqueConstraints = {
                @UniqueConstraint(
                name = "article_name_uk",
                columnNames = {"name"}),
                @UniqueConstraint(
                name = "article_slug_uk",
                columnNames = {"slug"})
        }
)
public class Article extends DomainEntity {

    public static final String NAME = "article";

    @Serial
    private static final long serialVersionUID = 851372619485730127L;

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

    @Column(name = "properties", nullable = false, columnDefinition = "JSON")
    private String properties;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @JsonIgnore
    @ManyToMany(fetch = LAZY)
    @JoinTable(
            name = "article_media",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    private List<Media> relatedMedia;

    @JsonIgnore
    @JsonProperty("category_slug")
    private transient String categorySlug;

    @JsonIgnore
    private transient Supplier<List<Media>> relatedMediaSupplier = () -> null;

    @JsonProperty("related_media")
    public List<Media> lazyLoadRelatedMedia() {
        return relatedMediaSupplier.get();
    }

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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public List<Media> getRelatedMedia() {
        return relatedMedia;
    }

    public void setRelatedMedia(List<Media> relatedMedia) {
        this.relatedMedia = relatedMedia;
    }

    public void setRelatedMediaSupplier(Supplier<List<Media>> relatedMediaSupplier) {
        this.relatedMediaSupplier = relatedMediaSupplier;
    }

}