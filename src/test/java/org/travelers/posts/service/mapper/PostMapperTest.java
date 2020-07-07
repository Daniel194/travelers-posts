package org.travelers.posts.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.travelers.posts.PostsApp;
import org.travelers.posts.domain.Post;
import org.travelers.posts.service.dto.PostDTO;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.travelers.posts.util.TestUtil.*;

@SpringBootTest(classes = PostsApp.class)
class PostMapperTest {

    @InjectMocks
    private PostMapper mapper;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void postsToPostDTOs() {
        List<PostDTO> postDTOs = mapper.postsToPostDTOs(Arrays.asList(getPost(), null));

        assertThat(postDTOs).isNotEmpty();
        assertThat(postDTOs).size().isEqualTo(1);
    }

    @Test
    void postToPostDTO() {
        Post post = getPost();
        PostDTO postDTO = mapper.postToPostDTO(post);

        assertThat(areEquals(post, postDTO)).isTrue();
    }

    @Test
    void postDTOsToPosts() {
        List<Post> posts = mapper.postDTOsToPosts(Arrays.asList(getPostDTO(), null));

        assertThat(posts).isNotEmpty();
        assertThat(posts).size().isEqualTo(1);
    }

    @Test
    void postDTOToPost() {
        PostDTO postDTO = getPostDTO();
        Post post = mapper.postDTOToPost(postDTO);

        assertThat(areEquals(post, postDTO)).isTrue();
    }

    @Test
    void mapToPost() throws JsonProcessingException {
        PostDTO postDTO = getPostDTO();
        String json = convertObjectToJson(postDTO);

        doReturn(postDTO).when(objectMapper).readValue(json, PostDTO.class);

        mapper.mapToPost(json);

        verify(objectMapper).readValue(json, PostDTO.class);
        verifyNoMoreInteractions(objectMapper);
    }

    @Test
    void mapToJson() throws JsonProcessingException {
        PostDTO postDTO = getPostDTO();
        String json = convertObjectToJson(postDTO);

        doReturn(json).when(objectMapper).writeValueAsString(postDTO);

        mapper.mapToJson(postDTO);

        verify(objectMapper).writeValueAsString(postDTO);
        verifyNoMoreInteractions(objectMapper);
    }
}
