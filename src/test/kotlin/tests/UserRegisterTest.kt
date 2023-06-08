package tests

import io.restassured.RestAssured
import lib.ApiCoreRequests
import lib.Assertions
import lib.BaseTestCase
import lib.DataGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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

    // Ex 15. Method 'user' tests
    @Test
    fun testCreateUserWithIncorrectEmail(){
        val userData = DataGenerator.getRegistrationData()
        userData["email"] = userData["email"]!!.replace("@", ".")

        val response = ApiCoreRequests()
            .makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
            )

        Assertions.assertResponseCodeEquals(response, 400)
        Assertions.assertResponseHasNotKey(response, "id")
        Assertions.assertHtmlByName(response, "body", "Invalid email format")
    }

    @ParameterizedTest
    @ValueSource(strings = ["email", "password", "firstName", "lastName", "username"])
    fun testCreateUserWithoutOneParam(param: String){
        val userData = DataGenerator.getRegistrationData()
        userData.remove(param)

        val response = ApiCoreRequests()
            .makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
            )

        Assertions.assertResponseCodeEquals(response, 400)
        Assertions.assertResponseHasNotKey(response, "id")
        Assertions.assertHtmlByName(response, "body","The following required params are missed: $param")
    }

    @Test
    fun testCreateUserWithTooShortName(){
        val userData = DataGenerator.getRegistrationData()
        userData["username"] = "a"

        val response = ApiCoreRequests()
            .makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
            )

        response.prettyPrint()
        Assertions.assertResponseCodeEquals(response, 400)
        Assertions.assertResponseHasNotKey(response, "id")
        Assertions.assertHtmlByName(response, "body", "The value of 'username' field is too short")
    }

    @Test
    fun testCreateUserWithTooLongName(){
        val userData = DataGenerator.getRegistrationData()
        userData["username"] = "a".repeat(251)

        val response = ApiCoreRequests()
            .makePostRequest(
                "https://playground.learnqa.ru/api/user/",
                userData
            )

        Assertions.assertResponseCodeEquals(response, 400)
        Assertions.assertResponseHasNotKey(response, "id")
        Assertions.assertHtmlByName(response, "body", "The value of 'username' field is too long")
    }
}