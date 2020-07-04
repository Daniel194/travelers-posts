package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.travelers.posts.service.dto.PostDTO;
import org.travelers.posts.web.rest.PostResource;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class UpdatePost extends StepDefs {

    @Autowired
    private PostResource postResource;

    private PostDTO postDTO;

    private MockMvc restUserMockMvc;

    @Before
    public void setup() {
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(postResource).build();
    }

    @Given("user wants to update a post with the following attributes")
    public void user_wants_update_post_wht_attributes(DataTable dataTable) {
        postDTO = (PostDTO) dataTable.asList(PostDTO.class).get(0);
    }

    @Given("user update the post {string}")
    public void user_update_post(String testCase) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        actions = restUserMockMvc.perform(put("/api/post")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(postDTO)));
    }

    @Then("the update is {string}")
    public void update_is(String expectedResult) {
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
