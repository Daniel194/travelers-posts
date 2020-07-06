package org.travelers.posts.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.undertow.util.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.travelers.posts.config.Constants;
import org.travelers.posts.security.SecurityUtils;
import org.travelers.posts.service.PostService;
import org.travelers.posts.service.dto.PostDTO;

import javax.validation.Valid;
import java.util.List;

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

    @GetMapping("/login/{login:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<List<PostDTO>> findAllByLogin(@PathVariable("login") String login) {
        return ResponseEntity.ok(postService.findByLogin(login));
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> findAllByLogin() {
        return ResponseEntity.ok(postService.findByLogin(SecurityUtils.getCurrentUserLogin().get()));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PostDTO> findAllById(@PathVariable("id") String id) {
        try {
            return ResponseEntity.ok(postService.findById(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<PostDTO> update(@Valid @RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.update(postDTO));
    }

}
