package tests

import io.restassured.RestAssured
import lib.Assertions
import lib.BaseTestCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class UserAuthTest: BaseTestCase() {

    var cookie: String = ""
    var header: String = ""
    var userAuthId: Int = 0

    @BeforeEach
    fun loginUser(){
        val authData = HashMap<String, String>()
        authData["email"] = "vinkotov@example.com"
        authData["password"] = "1234"

        val response = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login")
            .andReturn()

        this.cookie = getCookie(response, "auth_sid")
        this.header = getHeader(response, "x-csrf-token")
        this.userAuthId = getIntFromJson(response, "user_id")
    }

    @Test
    fun testAuthUser(){
        val responseCheckAuth = RestAssured
            .given()
            .header("x-csrf-token", header)
            .cookies("auth_sid", cookie)
            .get("https://playground.learnqa.ru/api/user/auth")
            .andReturn()

        Assertions().assertJsonByName(responseCheckAuth, "user_id", userAuthId)
    }

    @ParameterizedTest
    @ValueSource(strings = ["cookies", "headers"])
    fun testNegativeAuthUser(condition: String){
        val spec = RestAssured.given()
        spec.baseUri("https://playground.learnqa.ru/api/user/auth")

        if (condition.equals("cookies")){
            spec.cookie("auth_id", cookie)
        }else if (condition.equals("headers")){
            spec.header("x-csrf-token", header)
        }else{
            throw IllegalArgumentException("Condition value is unknown: $condition")
        }

        val responseForCheck = spec.get().andReturn()
        Assertions().assertJsonByName(responseForCheck, "user_id", 0)
    }
}