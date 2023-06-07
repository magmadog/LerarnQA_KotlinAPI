package tests

import io.restassured.RestAssured
import lib.Assertions
import lib.BaseTestCase
import lib.DataGenerator
import org.junit.jupiter.api.Test

class UserRegisterTest: BaseTestCase() {

    @Test
    fun testCreateUserWithExistingEmail(){
        var userData = HashMap<String, String>()

        val email = "vinkotov@example.com"
        userData["email"] = email
        userData = DataGenerator.getRegistrationData(userData)

        val response = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn()

        Assertions.assertResponseTextEquals(response, "Users with email '$email' already exists")
        Assertions.assertResponseCodeEquals(response, 400)
    }

    @Test
    fun testCreateUserSuccessfuly(){
        val userData = DataGenerator.getRegistrationData()

        val response = RestAssured
            .given()
            .body(userData)
            .post("https://playground.learnqa.ru/api/user/")
            .andReturn()

        Assertions.assertResponseCodeEquals(response, 200)
        Assertions.assertResponseHasKey(response, "id")
    }
}