package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.undertow.util.BadRequestException;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<PostDTO> findByLogin(String login) {
        return repository.findByLogin(login).stream()
            .map(PostDTO::new)
            .collect(Collectors.toList());
    }

    public PostDTO findById(String id) throws BadRequestException {
        Optional<Post> result = repository.findById(id);

        if (result.isPresent()) {
            return new PostDTO(result.get());
        }

        throw new BadRequestException();
    }

    public PostDTO update(PostDTO postDTO) {
        Post post = repository.save(mapper.postDTOToPost(postDTO));

        return mapper.postToPostDTO(post);
    }

}
