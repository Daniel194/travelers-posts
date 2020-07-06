package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.travelers.posts.config.KafkaProperties;
import org.travelers.posts.domain.Post;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.security.SecurityUtils;
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

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("add-country", objectMapper.writeValueAsString(getCountry(post))));

        return mapper.postToPostDTO(post);
    }

    public List<PostDTO> findByLogin(String login) {
        return repository.findByLogin(login).stream()
            .map(PostDTO::new)
            .collect(Collectors.toList());
    }

    public PostDTO findById(String id) throws ObjectNotFoundException {
        Optional<Post> result = repository.findById(id);

        if (result.isPresent()) {
            return new PostDTO(result.get());
        }

        throw new ObjectNotFoundException("Post not found");
    }

    public PostDTO update(PostDTO postDTO) {
        Post post = repository.save(mapper.postDTOToPost(postDTO));

        return mapper.postToPostDTO(post);
    }

    public boolean delete(String id) throws ObjectNotFoundException, JsonProcessingException {
        Post post = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (!post.getLogin().equalsIgnoreCase(login)) {
            throw new AccessDeniedException("Don't have permission to delete this post");
        }

        repository.delete(post);

        KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        producer.send(new ProducerRecord<>("remove-country", objectMapper.writeValueAsString(getCountry(post))));

        return true;
    }

    private CountryDTO getCountry(Post post) {
        CountryDTO countryDTO = new CountryDTO();
        countryDTO.setLogin(post.getLogin());
        countryDTO.setCountry(post.getCountry());

        return countryDTO;
    }

}
