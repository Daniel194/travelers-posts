import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for Post entity.
 */
class PostGatlingTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8081"""

    val httpConf = http
        .baseUrl(baseURL)
        .inferHtmlResources()
        .acceptHeader("*/*")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
        .connectionHeader("keep-alive")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
        .silentResources // Silence all resources like css or css so they don't clutter the results

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test Post entity")
        .exec(http("First unauthenticated request")
            .get("/api/post/admin")
            .headers(headers_http)
            .check(status.is(401))
        ).exitHereIfFailed
        .pause(10)
        .exec(http("Authentication")
            .post("/api/authenticate")
            .headers(headers_http_authentication)
            .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJson
            .check(header("Authorization").saveAs("access_token"))).exitHereIfFailed
        .pause(2)
        .exec(http("Authenticated request")
            .get("/api/post/admin")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
        .pause(10)
        .repeat(2) {
            exec(http("Get specific post by login")
                .get("/api/post/test")
                .headers(headers_http_authenticated)
                .check(status.is(200)))
                .pause(10 seconds, 20 seconds)
                .exec(http("Update current account")
                    .put("/api/post")
                    .headers(headers_http_authenticated)
                    .body(StringBody("""{
                 "description":"new description"
                 , "country":"TST"
                 }""")).asJson
                    .check(status.is(200))
                    .pause(10)
                    .repeat(5) {
                        exec(http("Get updated posts")
                            .get("/api/post")
                            .headers(headers_http_authenticated))
                            .pause(10)
                    }
        }

    val posts = scenario("Posts").exec(scn)

    setUp(
        posts.inject(rampUsers(Integer.getInteger("users", 100)) during (Integer.getInteger("ramp", 1) minutes))
    ).protocols(httpConf)
}
