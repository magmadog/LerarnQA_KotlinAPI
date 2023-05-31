package exercise

import io.restassured.RestAssured
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals


class Ex13UserAgent {

    companion object {
        @JvmStatic
        fun checkExplicitMethodSourceArgs(): Stream<TestUserAgent>? {
            return Stream.of(
                TestUserAgent(
                    "Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                    ExpectedUserAgentValues(
                        platform = "Mobile",
                        browser = "No",
                        device = "Android"
                    )
                ),
                TestUserAgent(
                    "Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                    ExpectedUserAgentValues(
                        platform = "Mobile",
                        browser = "Chrome",
                        device = "iOS"
                    )
                ),
                TestUserAgent(
                    "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                    ExpectedUserAgentValues(
                        platform = "Googlebot",
                        browser = "Unknown",
                        device = "Unknown"
                    )
                ),TestUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                    ExpectedUserAgentValues(
                        platform = "Web",
                        browser = "Chrome",
                        device = "No"
                    )
                ),TestUserAgent(
                    "Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                    ExpectedUserAgentValues(
                        platform = "Mobile",
                        browser = "No",
                        device = "iPhone"
                    )
                ),
            )
        }
    }

    @ParameterizedTest
    @MethodSource("checkExplicitMethodSourceArgs")
    fun testCheckUserAgents(testUserAgent: TestUserAgent){

        val headerUserAgent = testUserAgent.userAgentValue

        val response = RestAssured
            .given()
            .header("User-Agent", headerUserAgent)
            .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
            .jsonPath()

        assertAll(
            "Check User-Agent header",
            { assertEquals(
                testUserAgent.expectedUserAgent.platform,
                response.get("platform"),
                "Platform not equals")},
            {assertEquals(
                testUserAgent.expectedUserAgent.browser,
                response.get("browser"),
                "Browser not equals")},
            {assertEquals(
                testUserAgent.expectedUserAgent.device,
                response.get("device"),
                "Device not equals")}
        )
    }
}

data class TestUserAgent(
    val userAgentValue: String,
    val expectedUserAgent: ExpectedUserAgentValues)

data class ExpectedUserAgentValues(
    val platform: String,
    val browser: String,
    val device: String)