package exercise

import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Ex6GetRedirectURL {

    @Test
    fun test(){
        val response = RestAssured
            .given()
            .redirects()
            .follow(false)
            .get("https://playground.learnqa.ru/api/long_redirect")
            .andReturn()

        println("Redirecting url: ${response.headers["Location"].value}")
    }
}