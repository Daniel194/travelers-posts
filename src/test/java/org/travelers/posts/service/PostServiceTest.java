package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import javassist.tools.rmi.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.KafkaContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.travelers.posts.PostsApp;
import org.travelers.posts.config.KafkaProperties;
import org.travelers.posts.domain.Post;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.repository.search.PostSearchRepository;
import org.travelers.posts.service.dto.CountryDTO;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.service.mapper.PostMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.travelers.posts.util.TestUtil.getPost;
import static org.travelers.posts.util.TestUtil.getPostDTO;

@WithMockUser(value = "test")
@SpringBootTest(classes = PostsApp.class)
class PostServiceTest {
    private static KafkaContainer kafkaContainer;

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostSearchRepository postSearchRepository;

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
        doReturn(post).when(postSearchRepository).save(any(Post.class));
        doReturn("").when(objectMapper).writeValueAsString(any(CountryDTO.class));
        doReturn(postDTO).when(mapper).postToPostDTO(post);

        postService.create(postDTO);

        verify(kafkaProperties).getProducerProps();
        verify(mapper).postDTOToPost(postDTO);
        verify(postRepository).save(any(Post.class));
        verify(postSearchRepository).save(any(Post.class));
        verify(objectMapper).writeValueAsString(any(CountryDTO.class));
        verify(mapper).postToPostDTO(post);
        verifyNoMoreInteractions(kafkaProperties, mapper, postSearchRepository, postRepository, objectMapper);
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
    public void findById() throws ObjectNotFoundException {
        Post post = getPost();
        String id = "id";

        doReturn(Optional.of(post)).when(postRepository).findById(id);

        postService.findById(id);

        verify(postRepository).findById(id);
        verifyNoMoreInteractions(kafkaProperties, mapper, postRepository, objectMapper);
    }

    @Test
    public void update() {
        PostDTO postDTO = getPostDTO();
        Post post = getPost();

        postDTO.setId("test");
        post.setId("test");

        doReturn(post).when(mapper).postDTOToPost(postDTO);
        doReturn(post).when(postRepository).save(any(Post.class));
        doReturn(post).when(postSearchRepository).save(any(Post.class));
        doReturn(postDTO).when(mapper).postToPostDTO(post);

        postService.update(postDTO);

        verify(mapper).postDTOToPost(postDTO);
        verify(postRepository).save(any(Post.class));
        verify(postSearchRepository).save(any(Post.class));
        verify(mapper).postToPostDTO(post);
        verifyNoMoreInteractions(kafkaProperties, mapper, postRepository, postSearchRepository, objectMapper);
    }

    @Test
    public void deleteAuthorize() throws JsonProcessingException, ObjectNotFoundException {
        String id = "test";
        Post post = getPost();
        post.setLogin(id);

        doReturn(getProducerProps()).when(kafkaProperties).getProducerProps();
        doReturn(Optional.of(post)).when(postRepository).findById(id);
        doReturn("").when(objectMapper).writeValueAsString(any(CountryDTO.class));

        postService.delete(id);

        verify(kafkaProperties).getProducerProps();
        verify(postRepository).findById(id);
        verify(objectMapper).writeValueAsString(any(CountryDTO.class));
        verify(postRepository).delete(post);
        verify(postSearchRepository).delete(post);
        verifyNoMoreInteractions(kafkaProperties, mapper, postRepository, postSearchRepository, objectMapper);
    }

    @Test
    public void deleteNotAuthorize() {
        String id = "other";
        Post post = getPost();
        post.setLogin(id);

        doReturn(Optional.of(post)).when(postRepository).findById(id);

        assertThrows(AccessDeniedException.class, () -> postService.delete(id));

        verify(postRepository).findById(id);
        verifyNoMoreInteractions(kafkaProperties, mapper, postRepository, objectMapper);
    }

    @Test
    public void search() {
        String query = "test=query";
        List<Post> posts = Arrays.asList(getPost(), getPost(), getPost());

        doReturn(posts).when(postSearchRepository).search(queryStringQuery(query));

        assertThat(postService.search(query).size()).isEqualTo(posts.size());
    }

    private Map<String, Object> getProducerProps() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        return producerProps;
    }
}
