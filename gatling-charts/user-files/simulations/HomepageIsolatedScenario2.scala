
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HomepageIsolatedScenario2 extends Simulation {

	val domain = "www.namecheap.com"

	val httpProtocol = http
		.baseUrl("http://nc-home-page.production.disco.mss.namecheap.net")

	val OpenHomepage = scenario("OpenHomepage").during(30000 minutes) {
		exec(
			http("Get Homepage")
				.get("/")
		)
		.exec(flushCookieJar)
		.exec(flushHttpCache)
	}


	setUp(
		OpenHomepage.inject(atOnceUsers(150))
	).throttle(
		reachRps(50) in (1 minute),
		holdFor(20 minutes),
	).maxDuration(15 minute).protocols(httpProtocol)
}