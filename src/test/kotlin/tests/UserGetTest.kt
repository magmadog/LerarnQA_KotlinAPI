package tests

import io.restassured.RestAssured
import lib.ApiCoreRequests
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
        Assertions.assertResponseHasNotFields(response, listOf("firstName", "lastName", "email"))
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

    // Ex 16 Get one user when login as second user
    @Test
    fun testGetUserDataAuthAsAnotherUser(){
        val authData = HashMap<String, String>()
        authData["email"] = "vinkotov@example.com"
        authData["password"] = "1234"

        val response = ApiCoreRequests()
            .makePostRequest(
                "https://playground.learnqa.ru/api/user/login",
                authData
            )

        val cookie = getCookie(response, "auth_sid")
        val header = getHeader(response, "x-csrf-token")

        val responseWithAuth = ApiCoreRequests()
            .makeGetRequest(
                url = "https://playground.learnqa.ru/api/user/1",
                token = header,
                cookie = cookie
            )

        Assertions.assertResponseHasKey(responseWithAuth, "username")
        Assertions.assertResponseHasNotFields(responseWithAuth, listOf("firstName", "lastName", "email"))
    }
}