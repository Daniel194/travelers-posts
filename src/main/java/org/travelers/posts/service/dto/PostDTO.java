package org.travelers.posts.service.dto;

import org.travelers.posts.config.Constants;
import org.travelers.posts.domain.Post;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class PostDTO {

    private String id;

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 256)
    @NotNull
    private String coverImageUrl;

    @Size(max = 64)
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @Size(min = 3, max = 3)
    @NotNull
    private String country;

    @NotNull
    @Min(value = 1)
    @Max(value = 5)
    private Integer rating;

    public PostDTO() {
        // Empty constructor needed for Jackson.
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.login = post.getLogin();
        this.coverImageUrl = post.getCoverImageUrl();
        this.title = post.getTitle();
        this.description = post.getDescription();
        this.startDate = post.getStartDate();
        this.endDate = post.getEndDate();
        this.country = post.getCountry();
        this.rating = post.getRating();
    }

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
}
