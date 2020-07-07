package org.travelers.posts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.travelers.posts.config.KafkaProperties;
import org.travelers.posts.domain.Post;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.repository.search.PostSearchRepository;
import org.travelers.posts.security.SecurityUtils;
import org.travelers.posts.service.dto.CountryDTO;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.service.mapper.PostMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
public class PostService {

    private final PostRepository repository;
    private final PostSearchRepository searchRepository;
    private final PostMapper mapper;
    private final ObjectMapper objectMapper;
    private final KafkaProperties kafkaProperties;
    private final CacheManager cacheManager;

    @Autowired
    public PostService(PostRepository repository,
                       PostSearchRepository searchRepository,
                       PostMapper mapper,
                       ObjectMapper objectMapper,
                       KafkaProperties kafkaProperties,
                       CacheManager cacheManager) {
        this.repository = repository;
        this.searchRepository = searchRepository;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.kafkaProperties = kafkaProperties;
        this.cacheManager = cacheManager;
    }

    public PostDTO create(PostDTO postDTO) throws JsonProcessingException {
        Post post = repository.save(mapper.postDTOToPost(postDTO));

        searchRepository.save(post);
        clearUserCaches(post.getId());

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

        searchRepository.save(post);
        clearUserCaches(post.getId());

        return mapper.postToPostDTO(post);
    }

    public boolean delete(String id) throws ObjectNotFoundException, JsonProcessingException {
        Post post = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Post not found"));
        String login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new ObjectNotFoundException("User not found"));

        if (!post.getLogin().equalsIgnoreCase(login)) {
            throw new AccessDeniedException("Don't have permission to delete this post");
        }

        repository.delete(post);
        searchRepository.delete(post);
        clearUserCaches(post.getId());

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

    public List<PostDTO> search(String query) {
        return StreamSupport
            .stream(searchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(PostDTO::new)
            .collect(Collectors.toList());
    }

    private void clearUserCaches(String id) {
        Cache cache = cacheManager.getCache(PostRepository.POST_BY_ID_CACHE);

        if (cache != null) {
            cache.evict(id);
        }
    }

}
