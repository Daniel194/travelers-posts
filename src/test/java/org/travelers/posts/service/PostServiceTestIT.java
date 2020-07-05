package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.KafkaContainer;
import org.travelers.posts.PostsApp;
import org.travelers.posts.config.KafkaProperties;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.service.mapper.PostMapper;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.travelers.posts.util.TestUtil.getPostDTO;

@SpringBootTest(classes = PostsApp.class)
public class PostServiceTestIT {
    private static KafkaContainer kafkaContainer;

    @Autowired
    private PostRepository repository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private ObjectMapper objectMapper;

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

        postService = new PostService(repository, postMapper, objectMapper, kafkaProperties);
    }

    @Test
    public void create() throws JsonProcessingException {
        PostDTO send = getPostDTO();
        PostDTO received = postService.create(send);

        assertThat(received).isNotNull();
        assertThat(repository.findById(received.getId()).isPresent()).isTrue();
    }

    private Map<String, String> getProducerProps() {
        Map<String, String> producerProps = new HashMap<>();
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put("bootstrap.servers", kafkaContainer.getBootstrapServers());
        return producerProps;
    }
}
