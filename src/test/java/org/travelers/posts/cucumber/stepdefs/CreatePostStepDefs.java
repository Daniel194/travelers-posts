package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.travelers.posts.repository.PostRepository;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.web.rest.PostResource;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.travelers.posts.util.TestUtil.convertObjectToJsonBytes;

public class CreatePostStepDefs extends StepDefs {

    private final PostRepository postRepository;
    private final PostResource postResource;

    private PostDTO postDTO;
    private MockMvc restUserMockMvc;

    public CreatePostStepDefs(PostRepository postRepository, PostResource postResource) {
        this.postRepository = postRepository;
        this.postResource = postResource;
    }

    @Before
    public void setup() {
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(postResource).build();
    }

    @Given("user wants to create a post with the following attributes")
    public void user_create_post_with(DataTable dataTable) {
        postRepository.deleteAll();
        postDTO = (PostDTO) dataTable.asList(PostDTO.class).get(0);
    }

    @When("^user save the new post .*?")
    public void user_save_post() throws Exception {
        actions = restUserMockMvc.perform(post("/api/post")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(convertObjectToJsonBytes(postDTO)));
    }

    @Then("the save is {string}")
    public void save_is(String expectedResult) {
        int statusCode = actions.andReturn().getResponse().getStatus();

        switch (expectedResult) {
            case "SUCCESSFUL":
                assertThat(statusCode).isIn(200, 201);
                break;
            case "FAIL":
                assertThat(statusCode).isBetween(400, 504);
                break;
            default:
                fail("Unexpected error");
        }

    }

}
