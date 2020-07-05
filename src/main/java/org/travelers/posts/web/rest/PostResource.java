package org.travelers.posts.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.travelers.posts.service.PostService;
import org.travelers.posts.service.dto.PostDTO;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostResource {

    private final PostService postService;

    @Autowired
    public PostResource(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDTO> create(@Valid @RequestBody PostDTO postDTO) {
        try {
            return ResponseEntity.ok(postService.create(postDTO));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
