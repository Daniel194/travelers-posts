Feature: Delete post
    Delete post for a specific user

    Background:
        Given post with the following attributes
            | id  | login | coverImageUrl     | title     | description      | startDate  | endDate    | country | rating |
            | 400 | test  | http://other1.com | PotTitle1 | PostDescription1 | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | 500 | test  | http://other1.com | PotTitle1 | PostDescription1 | 2020-03-26 | 2020-04-02 | GRL     | 4      |

        When post already exists

    Scenario: Delete post SUCCESSFUL
        When login user 'test' wants to delete post with the id 400
        Then the delete is 'SUCCESSFUL'

    Scenario: Delete post FAIL
        When login user 'other' wants to delete post with the id 500
        Then the delete is 'FAIL'
