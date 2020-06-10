package io.gatling.highcharts

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BlazeDemo extends Simulation {

	val httpProtocol = http
		.baseUrl("https://www.demoblaze.com")
		.inferHtmlResources(BlackList(), WhiteList(""".*\www.demoblaze.com.*""",""".*\api.demoblaze.com.*"""))

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_1 = Map(
		"Accept" -> "application/json, text/javascript, */*; q=0.01",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-origin",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36",
		"X-Requested-With" -> "XMLHttpRequest")

	val headers_2 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Origin" -> "https://www.demoblaze.com",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_4 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Access-Control-Request-Headers" -> "content-type",
		"Access-Control-Request-Method" -> "POST",
		"Origin" -> "https://www.demoblaze.com",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_5 = Map(
		"Accept" -> "*/*",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Content-Type" -> "application/json",
		"Origin" -> "https://www.demoblaze.com",
		"Sec-Fetch-Dest" -> "empty",
		"Sec-Fetch-Mode" -> "cors",
		"Sec-Fetch-Site" -> "same-site",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_6 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "en-US,en;q=0.9",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "same-origin",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"User-Agent" -> "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")
	val uri1 = "https://api.demoblaze.com"

	val csvFeeder = csv("C:\\Users\\ashchoud2\\Bunnings\\src\\test\\resources\\data\\UserInformation.csv").circular

	val scn = scenario("BlazeDemo")

	.exec(flushHttpCache)
	.exec(flushCookieJar)
	.during(720)	{

			group("DemoBlaze_T01_HomePage") {

				exec(http("HomePage_EmbeddedResource1")
					.get("/index.html")
					.headers(headers_0)
					.check(substring("PRODUCT STORE")))
					.exec(http("HomePage_EmbeddedResource2")
						.get("/config.json")
						.headers(headers_1)
						.check(status.in(200, 304, 310)))
					.exec(http("HomePage_EmbeddedResource3")
						.get(uri1 + "/entries")
						.headers(headers_2)
						.check(substring("Samsung galaxy s6")))
					.exec { session =>
						println(session("c_AuthToken").as[String])
						session
					}
			}

				.pause(5)

				.feed(csvFeeder)
			.group("DemoBlaze_T02_Signin") {

				exec(http("SignIn_EmbeddedResource1")
					.options(uri1 + "/login")
					.headers(headers_4)
					.resources(http("SignIn_EmbeddedResource2")
						.post(uri1 + "/login")
						.headers(headers_5)
						.body(StringBody("""{"username":"${Username}","password":"cGFzc3dvcmQ="}""")).asJson
						.check(regex("""Auth_token:(.+?)"""").find.saveAs("c_AuthToken"))
						.check(status.is(200)),
						//.body(RawFileBody("io/gatling/highcharts/blazedemo/0005_request.json")),
						http("SignIn_EmbeddedResource3")
							.get("/index.html")
							.headers(headers_6)))
					.pause(1)
					.exec(http("SignIn_EmbeddedResource4")
						.get("/config.json")
						.headers(headers_1)
						.resources(http("SignIn_EmbeddedResource5")
							.options(uri1 + "/check")
							.headers(headers_4),
							http("SignIn_EmbeddedResource6")
								.get(uri1 + "/entries")
								.headers(headers_2)
								.check(substring("Items")),
							http("SignIn_EmbeddedResource7")
								.post(uri1 + "/check")
								.headers(headers_5)
								.body(StringBody("""{"token":"${c_AuthToken}"}""")).asJson))
					//	.body(RawFileBody("io/gatling/highcharts/blazedemo/0010_request.json"))))

					.exec { session =>
					println(session("c_AuthToken").as[String])
					session
				}
			}
				.pause(5)

			.group("DemoBlaze_T03_SelectCategory") {

				exec(http("SelectCategory_EmbeddedResource1")
					.options(uri1 + "/bycat")
					.headers(headers_4)
					.check(status.is(200))
					.resources(http("SelectCategory_EmbeddedResource2")
						.post(uri1 + "/bycat")
						.headers(headers_5)
						.body(StringBody("""{"cat":"phone"}""")).asJson
						.check(regex("""id":(.+?),"img""").findRandom.saveAs("c_PhoneID"))
						.check(substring("Sony Xperia"))))

					.exec { session =>
						println(session("c_PhoneID").as[String])
						session
					}
			}

				.pause(5)


			.group("DemoBlaze_T04_SelectProduct") {

				exec(http("SelectProduct_EmbeddedResource1")
					.get("/prod.html?idp_=${c_PhoneID}")
					.headers(headers_6)
					.check(status.in(200, 301, 304)))

					.exec(http("SelectProduct_EmbeddedResource2")
						.get("/config.json")
						.headers(headers_1)
						.resources(http("SelectProduct_EmbeddedResource3")
							.options(uri1 + "/check")
							.headers(headers_4),
							http("SelectProduct_EmbeddedResource4")
								.options(uri1 + "/view")
								.headers(headers_4),
							http("SelectProduct_EmbeddedResource5")
								.post(uri1 + "/check")
								.headers(headers_5)
								.body(StringBody("""{"token":"${c_AuthToken}"}""")).asJson,
							http("SelectProduct_EmbeddedResource6")
								.post(uri1 + "/view")
								.headers(headers_5)
								.body(StringBody("""{"id":"${c_PhoneID}"}""")).asJson
								.check(substring("price"))))
			}
				.pause(5)

			.group("DemoBlaze_T05_AddToCart") {
				exec(http("AddToCart_EmbeddedResource1")
					.options(uri1 + "/addtocart")
					.headers(headers_4)
					.check(status.in(200, 301, 304))
					.resources(http("AddToCart_EmbeddedResource1")
						.post(uri1 + "/addtocart")
						.headers(headers_5)
						.body(StringBody("""{"id":"0b39b359-76ef-3676-7e2b-979586221394","cookie":"${c_AuthToken}","prod_id":1,"flag":true}""")).asJson
						.check(status.in(200, 301, 304))))
			}
				.pause(5)
			.group("DemoBlaze_T06_Logout") {
				exec(http("Logout_EmbeddedResource1")
					.get("/index.html")
					.headers(headers_6)
					.check(status.in(200, 301, 304)))
					.exec(http("Logout_EmbeddedResource2")
						.get("/config.json")
						.headers(headers_1)
						.resources(http("Logout_EmbeddedResource3")
							.get(uri1 + "/entries")
							.headers(headers_2)
							.check(substring("Nokia Lumia"))))
			}	}
	setUp(scn.inject(rampUsers(2 )during(30 seconds),nothingFor(230 seconds),rampUsers(2) during(30 seconds),nothingFor(460 seconds),rampUsers(2)during(30 seconds))).protocols(httpProtocol)
}