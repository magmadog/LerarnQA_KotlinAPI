package tests

import io.restassured.RestAssured
import lib.Assertions
import lib.BaseTestCase
import org.junit.jupiter.api.Test

class UserGetTest: BaseTestCase() {

    @Test
    fun testGetUserDataNotAuth(){
        val response = RestAssured
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn()

        Assertions.assertResponseHasKey(response, "username")
        Assertions.assertResponseHasNotKey(response, "firstName")
        Assertions.assertResponseHasNotKey(response, "lastName")
        Assertions.assertResponseHasNotKey(response, "email")
    }

    @Test
    fun testGetUserDataAuthAsSameUser(){
        val authData = HashMap<String, String>()
        authData["email"] = "vinkotov@example.com"
        authData["password"] = "1234"

        val response = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn()

        val cookie = getCookie(response, "auth_sid")
        val header = getHeader(response, "x-csrf-token")

        val responseWithAuth = RestAssured
            .given()
            .header("x-csrf-token", header)
            .cookies("auth_sid", cookie)
            .get("https://playground.learnqa.ru/api/user/2")
            .andReturn()

        Assertions.assertResponseHasFields(responseWithAuth, listOf("username", "firstName", "lastName", "email"))
    }
}