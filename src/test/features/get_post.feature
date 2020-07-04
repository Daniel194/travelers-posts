Feature: Get all post
    Get all post for a specific user

    Background:
        Given post with the following attributes
            | id  | login | coverImageUrl     | title     | description      | startDate  | endDate    | country | rating |
            | 400 | test  | http://other1.com | PotTitle1 | PostDescription1 | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | 500 | test  | http://other2.com | PotTitle2 | PostDescription2 | 2020-04-02 | 2020-04-22 | ROM     | 5      |

        When post already exists

    Scenario: Get by login
        When a user wants to see all the pots of the user 'test'
        Then the response is 'SUCCESSFUL'
        And following posts are returned
            | id  | login | coverImageUrl     | title     | description      | startDate  | endDate    | country | rating |
            | 400 | other | http://other1.com | PotTitle1 | PostDescription1 | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | 500 | other | http://other2.com | PotTitle2 | PostDescription2 | 2020-04-02 | 2020-04-22 | ROM     | 5      |

    Scenario: Get current
        When user 'test' is login and wants to see all its posts
        Then the response is 'SUCCESSFUL'
        And following posts are returned
            | id  | login | coverImageUrl     | title     | description      | startDate  | endDate    | country | rating |
            | 400 | other | http://other1.com | PotTitle1 | PostDescription1 | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | 500 | other | http://other2.com | PotTitle2 | PostDescription2 | 2020-04-02 | 2020-04-22 | ROM     | 5      |

    Scenario: Get by id
        When a user wants to see a post with the id 400
        Then the response is 'SUCCESSFUL'
        And following post is returned
            | id  | login | coverImageUrl     | title     | description      | startDate  | endDate    | country | rating |
            | 400 | other | http://other1.com | PotTitle1 | PostDescription1 | 2020-03-26 | 2020-04-02 | GRL     | 4      |
