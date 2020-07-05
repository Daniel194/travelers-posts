package org.travelers.posts.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.travelers.posts.domain.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
}
