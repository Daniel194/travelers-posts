package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.datatable.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.springframework.cache.CacheManager;
import org.travelers.posts.domain.Post;
import org.travelers.posts.repository.PostRepository;

import java.util.List;

public class BackgroundStepDefs extends StepDefs {

    private final PostRepository postRepository;
    private final CacheManager cacheManager;

    private List<Post> posts;

    public BackgroundStepDefs(PostRepository postRepository, CacheManager cacheManager) {
        this.postRepository = postRepository;
        this.cacheManager = cacheManager;
    }

    @Given("post with the following attributes")
    public void post_with_following_attributes(DataTable dataTable) {
        DataTableType.entry(Post.class);
        this.posts = dataTable.asList(Post.class);
    }

    @When("post already exists")
    public void post_already_exists() {
        postRepository.deleteAll();
        postRepository.saveAll(posts);
        cacheManager.getCache(PostRepository.POST_BY_ID_CACHE).clear();
    }

}
