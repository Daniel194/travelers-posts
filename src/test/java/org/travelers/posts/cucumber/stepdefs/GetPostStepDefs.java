package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.web.rest.PostResource;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class GetPostStepDefs extends StepDefs {

    private final PostResource postResource;

    private MockMvc restUserMockMvc;

    public GetPostStepDefs(PostResource postResource) {
        this.postResource = postResource;
    }

    @Before
    public void setup() {
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(postResource).build();
    }

    @When("a user wants to see all the pots of the user {string}")
    public void user_wants_all_pots_of_user(String login) throws Exception {
        actions = restUserMockMvc.perform(get("/api/post/login/" + login)
            .accept(MediaType.APPLICATION_JSON));
    }

    @When("user {string} is login and wants to see all its posts")
    public void user_login_wants_all_its_posts(String login) throws Exception {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(login, login));
        SecurityContextHolder.setContext(securityContext);

        actions = restUserMockMvc.perform(get("/api/post")
            .accept(MediaType.APPLICATION_JSON));
    }

    @When("a user wants to see a post with the id {int}")
    public void user_wants_post_with_id(int id) throws Exception {
        actions = restUserMockMvc.perform(get("/api/post/id/" + id)
            .accept(MediaType.APPLICATION_JSON));
    }

    @Then("the response is {string}")
    public void response_is(String expectedResult) {
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

    @And("following posts are returned")
    public void posts_returned(DataTable dataTable) throws Exception {
        int size = dataTable.asList(PostDTO.class).size();

        actions
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.length()").value(size));
    }

    @And("following post is returned")
    public void post_returned(DataTable dataTable) throws Exception {
        PostDTO post = (PostDTO) dataTable.asList(PostDTO.class).get(0);

        actions
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(post.getId()))
            .andExpect(jsonPath("$.login").value(post.getLogin()))
            .andExpect(jsonPath("$.coverImageUrl").value(post.getCoverImageUrl()))
            .andExpect(jsonPath("$.title").value(post.getTitle()))
            .andExpect(jsonPath("$.description").value(post.getDescription()))
            .andExpect(jsonPath("$.country").value(post.getCountry()))
            .andExpect(jsonPath("$.rating").value(post.getRating()));
    }
}
