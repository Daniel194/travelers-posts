package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.core.api.TypeRegistry;
import io.cucumber.core.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;
import org.travelers.posts.domain.Post;
import org.travelers.posts.service.dto.PostDTO;

import java.time.LocalDate;
import java.util.Locale;

public class Configurer implements TypeRegistryConfigurer {
    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry registry) {
        registry.defineDataTableType(new DataTableType(Post.class, (TableEntryTransformer<Post>) entry -> {
            Post post = new Post();
            post.setId(entry.get("id"));
            post.setLogin(entry.get("login"));
            post.setCoverImageUrl(entry.get("coverImageUrl"));
            post.setTitle(entry.get("title"));
            post.setDescription(entry.get("description"));
            post.setStartDate(LocalDate.parse(entry.get("startDate")));
            post.setEndDate(LocalDate.parse(entry.get("endDate")));
            post.setCountry(entry.get("country"));
            post.setRating(Integer.parseInt(entry.get("rating")));

            return post;
        }));

        registry.defineDataTableType(new DataTableType(PostDTO.class, (TableEntryTransformer<PostDTO>) entry -> {
            PostDTO post = new PostDTO();
            post.setId(entry.get("id"));
            post.setLogin(entry.get("login"));
            post.setCoverImageUrl(entry.get("coverImageUrl"));
            post.setTitle(entry.get("title"));
            post.setDescription(entry.get("description"));
            post.setStartDate(LocalDate.parse(entry.get("startDate")));
            post.setEndDate(LocalDate.parse(entry.get("endDate")));
            post.setCountry(entry.get("country"));
            post.setRating(Integer.parseInt(entry.get("rating")));

            return post;
        }));
    }
}
