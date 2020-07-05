package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.travelers.posts.config.KafkaProperties;
import org.travelers.posts.domain.Post;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.service.dto.CountryDTO;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.service.mapper.PostMapper;

@Service
public class PostService {

    private final PostRepository repository;
    private final PostMapper mapper;
    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;

    @Autowired
    public PostService(PostRepository repository,
                       PostMapper mapper,
                       ObjectMapper objectMapper,
                       KafkaProperties kafkaProperties) {
        this.repository = repository;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.kafkaProperties = kafkaProperties;
    }

    public PostDTO create(PostDTO postDTO) throws JsonProcessingException {
        Post post = repository.save(mapper.postDTOToPost(postDTO));

        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setLogin(post.getLogin());
        countryDTO.setCountry(post.getCountry());

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("add-country", objectMapper.writeValueAsString(countryDTO)));

        return mapper.postToPostDTO(post);
    }

}
