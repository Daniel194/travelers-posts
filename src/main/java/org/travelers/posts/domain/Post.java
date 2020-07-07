package org.travelers.posts.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;
import org.travelers.posts.config.Constants;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@org.springframework.data.mongodb.core.mapping.Document(collection = "post")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "post_details")
public class Post extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Indexed
    private String login;

    @Size(max = 256)
    @Field("cover_image_url")
    @NotNull
    private String coverImageUrl;

    @Size(max = 64)
    @NotNull
    private String title;

    @NotNull
    private String description;

    @Field("start_date")
    @NotNull
    private LocalDate startDate;

    @Field("end_date")
    @NotNull
    private LocalDate endDate;

    @Size(min = 3, max = 3)
    @NotNull
    private String country;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id) &&
            Objects.equals(login, post.login) &&
            Objects.equals(coverImageUrl, post.coverImageUrl) &&
            Objects.equals(title, post.title) &&
            Objects.equals(description, post.description) &&
            Objects.equals(startDate, post.startDate) &&
            Objects.equals(endDate, post.endDate) &&
            Objects.equals(country, post.country) &&
            Objects.equals(rating, post.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, coverImageUrl, title, description, startDate, endDate, country, rating);
    }

    @Override
    public String toString() {
        return "Post{" +
            "id='" + id + '\'' +
            ", login='" + login + '\'' +
            ", coverImageUrl='" + coverImageUrl + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", country='" + country + '\'' +
            ", rating=" + rating +
            '}';
    }
}
