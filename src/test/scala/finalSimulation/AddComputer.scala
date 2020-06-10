package io.gatling.highcharts

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class AddComputer extends Simulation {

	val httpProtocol = http
		.baseUrl("https://computer-database.gatling.io")
	//	.inferHtmlResources(BlackList(), WhiteList(""".*computer-database.gatling.io.*"""))
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate, br")
		.acceptLanguageHeader("en-GB,en;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0")

		val headers_1 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:73.0) Gecko/20100101 Firefox/73.0")


	val headers_3 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_5 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "https://computer-database.gatling.io",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")


	val Computer_feeder = csv("C:\\Users\\ashchoud2\\Bunnings\\src\\test\\resources\\data\\ComputerDetails").random
	val scn = scenario("AddComputer")

  	.repeat(6) {

			exec(http("AddComputer_T01_LaunchPage")
				.get("/computers")
				.headers(headers_1)
				.check(substring("Computer database"))
				.check(status.is(200)))

				.pause(5)

				.exec(http("AddComputer_T02_ClickAddComputer")
					.get("/computers/new")
					.headers(headers_3)
					.check(regex("""><option value="(.+?)">""").findRandom.saveAs("c_Company"))
					//.check(regex("id_category=(.*?)&amp").findRandom.saveAs("cCategoryId"))
					.check(substring("Add a computer"))
					.check(status.is(200)))

				.pause(5)

				.feed(Computer_feeder)

				.exec(http("AddComputer_T03_AddComputer")
					.post("/computers")
					.headers(headers_5)
					.formParam("name", "${ComputerName}")
					.formParam("introduced", "${IntroducedDate}")
					.formParam("discontinued", "${DiscontinuedDate}")
					.formParam("company", "${c_Company}")
					.check(status.in(200 to 310)))

				.exec(http("AddComputer_T03_AddComputer")
					.get("/computers")
					.headers(headers_1)
					.check(substring("Computer database"))
					.check(status.is(200)))

				.exec(
					session => {
						println("c_Company = ")
						println(session("c_Company").as[String])
						session
					}
				)

				.pause(5)

		}
	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}