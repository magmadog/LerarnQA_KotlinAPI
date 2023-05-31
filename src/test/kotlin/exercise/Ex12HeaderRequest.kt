package exercise

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Ex12HeaderRequest {

    @Test
    fun testRecognizeHeader(){
        val response = RestAssured
            .get("https://playground.learnqa.ru/api/homework_header")
            .andReturn()

        val headers = response.headers
        println("Received headers: $headers")

        val secretHeaderName = "x-secret-homework-header"
        val secretHeaderValue = "Some secret value"

        assertTrue(headers.hasHeaderWithName(secretHeaderName), "Response doesn't have '$secretHeaderName' cookie")
        assertEquals(
            secretHeaderValue,
            headers.getValue(secretHeaderName),
            "Cookie doesn't have cookie '$secretHeaderName' with value '$secretHeaderValue'"
        )
    }
}