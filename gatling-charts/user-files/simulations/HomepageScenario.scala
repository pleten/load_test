
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class HomepageScenario extends Simulation {

	val domain = "www.namecheap.com"

	val httpProtocol = http
		.baseUrl("http://www.namecheap.com")

	val OpenHomepage = scenario("OpenHomepage").during(30000 minutes) {
		exec(addCookie(Cookie("product_platform","thebest").withDomain("www.namecheap.com")))
		.exec(
			http("Get Homepage")
				.get("/home")
		)
		.exec(flushCookieJar)
		.exec(flushHttpCache)
	}


	setUp(
		OpenHomepage.inject(atOnceUsers(20), rampUsers(1000) during (20 minutes), nothingFor(30 minutes))
	).throttle(
		reachRps(0) in (5 minutes),
		reachRps(0) in (5 minutes),
		holdFor(5 minutes),
		reachRps(0) in (5 minutes),
		holdFor(10 minutes),
		jumpToRps(0),
		holdFor(2 minutes),
	).maxDuration(50 minute).protocols(httpProtocol)
}