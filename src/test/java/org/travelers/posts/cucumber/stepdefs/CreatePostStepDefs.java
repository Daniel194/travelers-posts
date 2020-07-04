package org.travelers.posts.cucumber.stepdefs;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreatePostStepDefs extends StepDefs {

    @Given("user wants to create a post with the following attributes")
    public void user_create_post_with(DataTable dataTable) {

    }

    @When("user save the new post {string}")
    public void user_save_post(String testContext) {

    }

    @Then("the save is {string}")
    public void save_is(String expectedResult) {

    }

}
