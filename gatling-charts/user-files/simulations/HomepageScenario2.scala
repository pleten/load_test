
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HomepageScenario2 extends Simulation {

	val domain = "www.namecheap.com"

	val httpProtocol = http
		.baseUrl("http://www.namecheap.com").inferHtmlResources

	val OpenHomepage = scenario("OpenHomepage").during(30 minutes) {
		exec(
			http("Get Homepage")
				.get("/home")
		)
		.exec(flushCookieJar)
		.exec(flushHttpCache)
	}


	setUp(
		OpenHomepage.inject(atOnceUsers(2), rampUsers(10) during (20 seconds), nothingFor(30 minutes))
	).throttle(
		reachRps(5) in (15 seconds),
		holdFor(20 seconds),
		reachRps(3) in (15 seconds),
		holdFor(1 minute)
	).maxDuration(2 minute).protocols(httpProtocol)
}