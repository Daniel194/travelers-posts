Feature: Update post
    Update post for a specific user

    Background:
        Given post with the following attributes
            | id  | login | coverImageUrl     | title     | description      | startDate  | endDate    | country | rating |
            | 400 | other | http://other1.com | PotTitle1 | PostDescription1 | 2020-03-26 | 2020-04-02 | GRL     | 4      |

        When post already exists

    Scenario Outline: Update post <testCase> <expectedResult>
        Given user wants to update a post with the following attributes
            | id   | login   | coverImageUrl   | title   | description   | startDate   | endDate   | country   | rating   |
            | <id> | <login> | <coverImageUrl> | <title> | <description> | <startDate> | <endDate> | <country> | <rating> |
        When user update the new post '<testCase>'
        Then the update is '<expectedResult>'
        Examples:
            | testCase            | expectedResult | id  | login | coverImageUrl    | title    | description     | startDate  | endDate    | country | rating |
            | WITH ALL FIELDS     | SUCCESSFUL     | 400 | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT LOGIN       | FAIL           | 400 |       | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT IMAGE       | FAIL           | 400 | other |                  | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT TITLE       | FAIL           | 400 | other | http://other.com |          | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT DESCRIPTION | FAIL           | 400 | other | http://other.com | PotTitle |                 | 2020-03-26 | 2020-04-02 | GRL     | 4      |
            | WITHOUT START DATE  | FAIL           | 400 | other | http://other.com | PotTitle | PostDescription |            | 2020-04-02 | GRL     | 4      |
            | WITHOUT END DATE    | FAIL           | 400 | other | http://other.com | PotTitle | PostDescription | 2020-03-26 |            | GRL     | 4      |
            | WITHOUT COUNTRY     | FAIL           | 400 | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 |         | 4      |
            | WITHOUT RATING      | FAIL           | 400 | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     |        |
            | WITHOUT HIGH RATING | FAIL           | 400 | other | http://other.com | PotTitle | PostDescription | 2020-03-26 | 2020-04-02 | GRL     | 10     |
