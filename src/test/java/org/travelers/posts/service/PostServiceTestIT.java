package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.tools.rmi.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.KafkaContainer;
import org.travelers.posts.PostsApp;
import org.travelers.posts.config.KafkaProperties;
import org.travelers.posts.domain.Post;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.repository.search.PostSearchRepository;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.service.mapper.PostMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.travelers.posts.util.TestUtil.*;

@WithMockUser(value = "test")
@SpringBootTest(classes = PostsApp.class)
public class PostServiceTestIT {
    private static KafkaContainer kafkaContainer;

    @Autowired
    private PostRepository repository;

    @Autowired
    private PostSearchRepository searchRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CacheManager cacheManager;

    private PostService postService;

    @BeforeAll
    static void startServer() {
        kafkaContainer = new KafkaContainer("5.4.0");
        kafkaContainer.start();
    }

    @BeforeEach
    void setup() {
        KafkaProperties kafkaProperties = new KafkaProperties();
        kafkaProperties.setProducer(getProducerProps());

        postService = new PostService(repository, searchRepository, postMapper, objectMapper, kafkaProperties, cacheManager);
    }

    @Test
    public void create() throws JsonProcessingException {
        PostDTO send = getPostDTO();
        PostDTO received = postService.create(send);

        assertThat(received).isNotNull();
        assertThat(repository.findById(received.getId()).isPresent()).isTrue();
    }

    @Test
    public void findByLogin() {
        Post post = getPost();

        repository.save(post);

        List<PostDTO> receive = postService.findByLogin(post.getLogin());

        assertThat(receive.size()).isEqualTo(1);
        assertThat(areEquals(post, receive.get(0))).isTrue();
    }

    @Test
    public void findById() throws ObjectNotFoundException {
        String id = "test";
        Post post = getPost();
        post.setId(id);

        repository.save(post);

        PostDTO receive = postService.findById(id);

        assertThat(receive).isNotNull();
        assertThat(areEquals(post, receive)).isTrue();
    }

    @Test
    public void update() throws JsonProcessingException {
        PostDTO post = postService.create(getPostDTO());

        post.setDescription("New Description");

        PostDTO receive = postService.update(post);

        assertThat(receive.getDescription()).isEqualTo(post.getDescription());
    }

    @Test
    public void deleteAuthorize() throws JsonProcessingException, ObjectNotFoundException {
        Post post = getPost();
        post.setLogin("test");

        Post saved = repository.save(post);

        postService.delete(saved.getId());

        assertThat(repository.findById(post.getLogin()).isPresent()).isFalse();
    }

    @Test
    public void deleteNotAuthorize() {
        Post post = getPost();
        post.setLogin("other");

        Post saved = repository.save(post);

        assertThrows(AccessDeniedException.class, () -> postService.delete(saved.getId()));
    }

    private Map<String, String> getProducerProps() {
        Map<String, String> producerProps = new HashMap<>();
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        return producerProps;
    }
}
