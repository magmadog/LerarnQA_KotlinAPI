package tests

import io.restassured.RestAssured
import lib.ApiCoreRequests
import lib.Assertions
import lib.BaseTestCase
import lib.DataGenerator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNotEquals

class UserEditTest: BaseTestCase() {

    val testUserEmail = "learqa20230608105415@example.com"
    val testUserPassword = "1234"
    var cookie: String = ""
    var header: String = ""
    var userAuthId: Int = 0
    private val apiCoreRequests = ApiCoreRequests()

    @BeforeEach
    fun loginTestUser(){
        val authData = HashMap<String, String>()
        authData["email"] = testUserEmail
        authData["password"] = testUserPassword

        val response =  apiCoreRequests.makePostRequest(
            "https://playground.learnqa.ru/api/user/login",
            authData
        )

        this.cookie = getCookie(response, "auth_sid")
        this.header = getHeader(response, "x-csrf-token")
        this.userAuthId = getIntFromJson(response, "user_id")
    }

    @Test
    fun testEditJustCreatedTest(){
        val userData = DataGenerator.getRegistrationData()

        print(userData)
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

    //Ex 17 Negative edit tests

    @Test
    fun testEditUserWithoutAuth(){
        val newName = "Changed Name"
        val newData = HashMap<String, String>()
        newData["firstName"] = newName

        val responseEdit = ApiCoreRequests()
            .makePutRequest(
                url = "https://playground.learnqa.ru/api/user/2",
                token = "",
                cookie = "",
                id = 2,
                newData = newData
            )

        Assertions.assertResponseCodeEquals(responseEdit, 400)
        Assertions.assertHtmlByName(responseEdit, "body","Auth token not supplied")
    }

    @Test
    fun testEditUserByLoginAsAnotherUser(){
        val newName = "changed"
        val newData = HashMap<String, String>()
        newData["username"] = newName

        val testUserId = 72380

        val responseBeforeEdit = RestAssured
            .get("https://playground.learnqa.ru/api/user/$testUserId")
            .andReturn()

        val responseEdit = ApiCoreRequests()
            .makePutRequest(
                url = "https://playground.learnqa.ru/api/user/",
                token = header,
                cookie = cookie,
                id = testUserId,
                newData = newData
            )

        val responseAfterEdit = RestAssured
            .get("https://playground.learnqa.ru/api/user/$testUserId")
            .andReturn()

        Assertions.assertResponseCodeEquals(responseEdit, 200)
        assertNotEquals(
            newName,
            responseAfterEdit.jsonPath().get("username"),
            "Username has changed, but it not right"
        )
    }

    @Test
    fun testEditUserByIncorrectEmail(){
        val newData = HashMap<String, String>()
        val newEmail = testUserEmail.replace("@", ".")
        newData["email"] = newEmail

        val responseEdit = ApiCoreRequests()
            .makePutRequest(
                url = "https://playground.learnqa.ru/api/user/",
                token = header,
                cookie = cookie,
                id = this.userAuthId,
                newData = newData
            )

        Assertions.assertHtmlByName(responseEdit, "body", "Invalid email format")
        Assertions.assertResponseCodeEquals(responseEdit, 400)
    }

    @Test
    fun testEditUserByTooShortFirstName(){
        val newData = HashMap<String, String>()
        val newFirstName = "A"
        newData["firstName"] = newFirstName

        val responseEdit = ApiCoreRequests()
            .makePutRequest(
                url = "https://playground.learnqa.ru/api/user/",
                token = header,
                cookie = cookie,
                id = this.userAuthId,
                newData = newData
            )

        Assertions.assertJsonByName(responseEdit, "error", "Too short value for field firstName")
        Assertions.assertResponseCodeEquals(responseEdit, 400)
    }
}