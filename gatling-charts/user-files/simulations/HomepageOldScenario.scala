
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HomepageOldScenario extends Simulation {

	val domain = "www.namecheap.com"

	val httpProtocol = http
		.baseUrl("http://www.namecheap.com")

	val OpenHomepage = scenario("OpenHomepage").during(30000 minutes) {
		exec(
			http("Get Homepage")
				.get("/")
		)
		.exec(flushCookieJar)
		.exec(flushHttpCache)
	}


	setUp(
		OpenHomepage.inject(atOnceUsers(10), rampUsers(1000) during (25 minutes), nothingFor(30 minutes))
	).throttle(
		reachRps(0) in (2 minutes),
		holdFor(2 minutes),
		reachRps(0) in (2 minutes),
		holdFor(2 minutes),
		reachRps(0) in (2 minutes),
		holdFor(2 minutes),
		reachRps(0) in (2 minutes),
		holdFor(2 minutes),
		reachRps(0) in (2 minutes),
		holdFor(2 minutes),
		jumpToRps(0),
		holdFor(2 minutes),
	).maxDuration(25 minute).protocols(httpProtocol)
}