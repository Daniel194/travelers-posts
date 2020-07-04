Feature: Create new post
    Create post based on information received from the user

    Scenario Outline: Create post <testCase> <expectedResult>
        Given user wants to create a post with the following attributes
            | login   | coverImageUrl   | title   | description   | startDate   | endDate   | country   | rating   |
            | <login> | <coverImageUrl> | <title> | <description> | <startDate> | <endDate> | <country> | <rating> |
        When user save the new post '<testCase>'
        Then the save is '<expectedResult>'
        Examples:
            | testCase            | expectedResult | login | coverImageUrl    | title    | description     | startDate  | endDate    | country | rating |
            | WITH ALL FIELDS     | SUCCESSFUL     | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT LOGIN       | FAIL           |       | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT IMAGE       | FAIL           | other |                  | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT TITLE       | FAIL           | other | http://other.com |          | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT DESCRIPTION | FAIL           | other | http://other.com | PotTitle |                 | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT START DATE  | FAIL           | other | http://other.com | PotTitle | PostDescription |            | 2020-04-02 | GRL     | 4      |
            | WITHOUT END DATE    | FAIL           | other | http://other.com | PotTitle | PostDescription | 2020-03-26 |            | GRL     | 4      |
            | WITHOUT COUNTRY     | FAIL           | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 |         | 4      |
            | WITHOUT RATING      | FAIL           | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     |        |
            | WITHOUT HIGH RATING | FAIL           | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 10     |
