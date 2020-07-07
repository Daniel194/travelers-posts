package org.travelers.posts.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.travelers.posts.config.Constants;
import org.travelers.posts.security.AuthoritiesConstants;
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
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<PostDTO> update(@Valid @RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.update(postDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        try {
            if (postService.delete(id)) {
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        } catch (ObjectNotFoundException | JsonProcessingException | AccessDeniedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/_search/posts/{query}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<PostDTO> search(@PathVariable String query) {
        return postService.search(query);
    }

}
