package org.travelers.posts.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.travelers.posts.domain.Post;

@Repository
public interface PostSearchRepository extends ElasticsearchRepository<Post, String> {
}
