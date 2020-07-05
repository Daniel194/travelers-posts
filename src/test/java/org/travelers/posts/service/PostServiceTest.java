package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.undertow.util.BadRequestException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.KafkaContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.travelers.posts.PostsApp;
import org.travelers.posts.config.KafkaProperties;
import org.travelers.posts.domain.Post;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.service.dto.CountryDTO;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.service.mapper.PostMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.travelers.posts.util.TestUtil.getPost;
import static org.travelers.posts.util.TestUtil.getPostDTO;

@SpringBootTest(classes = PostsApp.class)
class PostServiceTest {
    private static KafkaContainer kafkaContainer;

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper mapper;

    @Mock
    private KafkaProperties kafkaProperties;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeAll
    static void startServer() {
        kafkaContainer = new KafkaContainer("5.4.0");
        kafkaContainer.start();
    }

    @Test
    void create() throws JsonProcessingException {
        PostDTO postDTO = getPostDTO();
        Post post = getPost();

        doReturn(getProducerProps()).when(kafkaProperties).getProducerProps();
        doReturn(post).when(mapper).postDTOToPost(postDTO);
        doReturn(post).when(postRepository).save(any(Post.class));
        doReturn("").when(objectMapper).writeValueAsString(any(CountryDTO.class));
        doReturn(postDTO).when(mapper).postToPostDTO(post);

        postService.create(postDTO);

        verify(kafkaProperties).getProducerProps();
        verify(mapper).postDTOToPost(postDTO);
        verify(postRepository).save(any(Post.class));
        verify(objectMapper).writeValueAsString(any(CountryDTO.class));
        verify(mapper).postToPostDTO(post);
        verifyNoMoreInteractions(kafkaProperties, mapper, postRepository, objectMapper);
    }

    @Test
    public void findByLogin() {
        Post post = getPost();
        String login = "test";

        doReturn(Collections.singletonList(post)).when(postRepository).findByLogin(login);

        postService.findByLogin(login);

        verify(postRepository).findByLogin(login);
        verifyNoMoreInteractions(kafkaProperties, mapper, postRepository, objectMapper);
    }

    @Test
    public void findById() throws BadRequestException {
        Post post = getPost();
        String id = "id";

        doReturn(Optional.of(post)).when(postRepository).findById(id);

        postService.findById(id);

        verify(postRepository).findById(id);
        verifyNoMoreInteractions(kafkaProperties, mapper, postRepository, objectMapper);
    }

    private Map<String, Object> getProducerProps() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        return producerProps;
    }
}
