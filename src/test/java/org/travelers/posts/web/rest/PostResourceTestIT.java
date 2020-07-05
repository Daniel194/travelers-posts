package org.travelers.posts.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.travelers.posts.PostsApp;
import org.travelers.posts.service.dto.PostDTO;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.travelers.posts.util.TestUtil.convertObjectToJsonBytes;
import static org.travelers.posts.util.TestUtil.getPostDTO;

@AutoConfigureMockMvc
@WithMockUser(value = "test")
@SpringBootTest(classes = PostsApp.class)
class PostResourceTestIT {

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void create() throws Exception {
        PostDTO postDTO = getPostDTO();

        String response = restUserMockMvc.perform(post("/api/post")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(postDTO)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PostDTO postDTOResponse = mapper.readValue(response, PostDTO.class);

        assertThat(postDTOResponse.getId()).isNotNull();
        assertThat(postDTOResponse.getLogin()).isEqualTo(postDTO.getLogin());
    }
}