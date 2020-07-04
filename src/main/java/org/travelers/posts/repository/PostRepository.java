package org.travelers.posts.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.travelers.posts.domain.Post;

public interface PostRepository extends MongoRepository<Post, String> {
}
