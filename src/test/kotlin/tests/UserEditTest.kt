package tests

import io.restassured.RestAssured
import lib.Assertions
import lib.BaseTestCase
import lib.DataGenerator
import org.junit.jupiter.api.Test

class UserEditTest: BaseTestCase() {

    @Test
    fun testEditJustCreatedTest(){
        val userData = DataGenerator.getRegistrationData()

        val responseCreateUser = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .jsonPath()

        val userId = responseCreateUser.getString("id")

        val authData = HashMap<String, String>()
        authData["email"] = userData.getValue("email")
        authData["password"] = userData.getValue("password")

        val responseAuth = RestAssured
            .given()
            .body(authData)
            .post("https://playground.learnqa.ru/api/user/login/")
            .andReturn()

        val newName = "Changed Name"
        val newData = HashMap<String, String>()
        newData["firstName"] = newName

        val responseEdit = RestAssured
            .given()
            .header("x-csrf-token", getHeader(responseAuth, "x-csrf-token"))
            .cookie("auth_sid", getCookie(responseAuth, "auth_sid"))
            .body(newData)
            .put("https://playground.learnqa.ru/api/user/$userId")
            .andReturn()

        val responseAfterEdit = RestAssured
            .given()
            .header("x-csrf-token", getHeader(responseAuth, "x-csrf-token"))
            .cookie("auth_sid", getCookie(responseAuth, "auth_sid"))
            .get("https://playground.learnqa.ru/api/user/$userId")
            .andReturn()

        Assertions.assertJsonByName(responseAfterEdit, "firstName", newName)
    }
}