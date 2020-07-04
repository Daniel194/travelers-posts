package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DeletePostStepDefs extends StepDefs {

    @When("login user {string} wants to delete post with the id {int}")
    public void user_wants_delete_post_with_id(String login, int id) {

    }

    @Then("the delete is {string}")
    public void delete_is(String expectedResult) {

    }

}
