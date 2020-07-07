package org.travelers.posts.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.travelers.posts.domain.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    String POST_BY_ID_CACHE = "postById";

    @Cacheable(cacheNames = POST_BY_ID_CACHE)
    @Override
    Optional<Post> findById(String id);

    List<Post> findByLogin(String login);

}
