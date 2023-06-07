package tests

import io.qameta.allure.Description
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.restassured.RestAssured
import lib.ApiCoreRequests
import lib.Assertions
import lib.BaseTestCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@Epic("Authorization Cases")
@Feature("Authorization")
class UserAuthTest: BaseTestCase() {

    var cookie: String = ""
    var header: String = ""
    var userAuthId: Int = 0
    private val apiCoreRequests = ApiCoreRequests()

    @BeforeEach
    fun loginUser(){
        val authData = HashMap<String, String>()
        authData["email"] = "vinkotov@example.com"
        authData["password"] = "1234"

        val response =  apiCoreRequests.makePostRequest(
            "https://playground.learnqa.ru/api/user/login",
            authData
        )

        this.cookie = getCookie(response, "auth_sid")
        this.header = getHeader(response, "x-csrf-token")
        this.userAuthId = getIntFromJson(response, "user_id")
    }

    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    fun testAuthUser(){
        val responseCheckAuth = apiCoreRequests.makeGetRequest(
            "https://playground.learnqa.ru/api/user/auth",
            header,
            cookie
        )

        Assertions.assertJsonByName(responseCheckAuth, "user_id", userAuthId)
    }

    @Description("This test check authorization status sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = ["cookies", "headers"])
    fun testNegativeAuthUser(condition: String){
        val spec = RestAssured.given()
        spec.baseUri("https://playground.learnqa.ru/api/user/auth")

        if (condition.equals("cookies")) {
            val responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                "https://playground.learnqa.ru/api/user/auth",
                this.cookie
            )
            Assertions.assertJsonByName(responseForCheck, "user_id", 0)
        }else if (condition.equals("headers")){
            val responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                "https://playground.learnqa.ru/api/user/auth",
                this.header
            )
            Assertions.assertJsonByName(responseForCheck, "user_id", 0)
        }else{
            throw IllegalArgumentException("Condition value is unknown: $condition")
        }
    }
}