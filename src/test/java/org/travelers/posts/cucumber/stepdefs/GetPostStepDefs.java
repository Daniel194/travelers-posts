package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class GetPostStepDefs extends StepDefs {

    @When("a user wants to see all the pots of the user {string}")
    public void user_wants_all_pots_of_user(String login) {

    }

    @When("user {string} is login and wants to see all its posts")
    public void user_login_wants_all_its_posts(String login) {

    }

    @When("a user wants to see a post with the id {int}")
    public void user_wants_post_with_id(int id) {

    }

    @Then("the response is {string}")
    public void response_is(String expectedResult) {

    }

    @And("following post/posts is/are returned")
    public void account_details_returned(DataTable dataTable) {

    }

}
