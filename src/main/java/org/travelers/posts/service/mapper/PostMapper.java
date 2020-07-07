package org.travelers.posts.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travelers.posts.domain.Post;
import org.travelers.posts.service.dto.PostDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PostMapper {

    private final ObjectMapper objectMapper;

    @Autowired
    public PostMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<PostDTO> postsToPostDTOs(List<Post> posts) {
        return posts.stream()
            .filter(Objects::nonNull)
            .map(this::postToPostDTO)
            .collect(Collectors.toList());
    }

    public PostDTO postToPostDTO(Post post) {
        return new PostDTO(post);
    }

    public List<Post> postDTOsToPosts(List<PostDTO> postDTOs) {
        return postDTOs.stream()
            .filter(Objects::nonNull)
            .map(this::postDTOToPost)
            .collect(Collectors.toList());
    }

    public Post postDTOToPost(PostDTO postDTO) {
        if (postDTO == null) {
            return null;
        }

        Post post = new Post();
        post.setId(postDTO.getId());
        post.setLogin(postDTO.getLogin());
        post.setCoverImageUrl(postDTO.getCoverImageUrl());
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setStartDate(postDTO.getStartDate());
        post.setEndDate(postDTO.getEndDate());
        post.setCountry(postDTO.getCountry());
        post.setRating(postDTO.getRating());

        return post;
    }

    public PostDTO mapToPost(String post) throws JsonProcessingException {
        return objectMapper.readValue(post, PostDTO.class);
    }

    public String mapToJson(PostDTO post) throws JsonProcessingException {
        return objectMapper.writeValueAsString(post);
    }

}
