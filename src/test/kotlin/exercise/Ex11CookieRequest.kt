package exercise

import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Ex11CookieRequest {

    @Test
    fun testRecognizeCookie(){
        val response = RestAssured
            .get("https://playground.learnqa.ru/api/homework_cookie")
            .andReturn()

        val cookies = response.cookies
        println("Received cookie: $cookies")

        val cookieName = "HomeWork"
        val cookieValue = "hw_value"

        assertTrue(cookies.containsKey(cookieName), "Response doesn't have '$cookieName' cookie")
        assertEquals(
            cookieValue,
            cookies["HomeWork"],
            "Cookie doesn't have cookie '$cookieName' with value '$cookieValue'"
        )
    }
}