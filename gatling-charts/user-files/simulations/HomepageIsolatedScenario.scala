
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HomepageIsolatedScenario extends Simulation {

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
		OpenHomepage.inject(atOnceUsers(10), rampUsers(1500) during (20 minutes), nothingFor(30 minutes))
	).throttle(
		reachRps(0) in (5 minutes),
		holdFor(5 minutes),
	).maxDuration(16 minute).protocols(httpProtocol)
}