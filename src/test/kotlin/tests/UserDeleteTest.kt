package tests

import lib.ApiCoreRequests
import lib.Assertions
import lib.BaseTestCase
import lib.DataGenerator
import org.junit.jupiter.api.Test

class UserDeleteTest: BaseTestCase() {

    val apiCoreRequests = ApiCoreRequests()

    @Test
    fun testTryToDeleteUserWithId2(){
        val authData = HashMap<String, String>()
        authData["email"] = "vinkotov@example.com"
        authData["password"] = "1234"

        val response =  apiCoreRequests
            .makePostRequest(
            url = "https://playground.learnqa.ru/api/user/login",
            authData = authData
        )

        val cookie = getCookie(response, "auth_sid")
        val header = getHeader(response, "x-csrf-token")
        val userAuthId = getIntFromJson(response, "user_id")

        val requestForDelete = apiCoreRequests
            .makeDeleteRequest(
                url = "https://playground.learnqa.ru/api/user/",
                id = userAuthId,
                token = header,
                cookie = cookie
            )

        Assertions.assertResponseCodeEquals(requestForDelete, 400)
        Assertions.assertHtmlByName(requestForDelete, "body", "Please, do not delete test users with ID 1, 2, 3, 4 or 5.")
    }

    @Test
    fun testPositiveDelete(){
        val userData = DataGenerator.getRegistrationData()

        val responseCreateUser = apiCoreRequests
            .makePostRequest(
                url = "https://playground.learnqa.ru/api/user/",
                authData = userData
            )

        val authData = HashMap<String, String>()
        authData["email"] = userData.getValue("email")
        authData["password"] = userData.getValue("password")

        val responseAuth = apiCoreRequests
            .makePostRequest(
                url = "https://playground.learnqa.ru/api/user/login/",
                authData = authData
            )

        val token = getHeader(responseAuth, "x-csrf-token")
        val cookie = getCookie(responseAuth, "auth_sid")
        val userId = getIntFromJson(responseAuth, "user_id")

        val responseDelete = apiCoreRequests
            .makeDeleteRequest(
                url = "https://playground.learnqa.ru/api/user/",
                id = userId,
                token = token,
                cookie = cookie
            )

        val responseCheckDeleteUser = apiCoreRequests
            .makeGetRequest(
                url = "https://playground.learnqa.ru/api/user/$userId",
                token = token,
                cookie = cookie
            )

        Assertions.assertResponseCodeEquals(responseCheckDeleteUser, 404)
        Assertions.assertHtmlByName(responseCheckDeleteUser, "body", "User not found")
    }

    @Test
    fun testDeleteUserFromAnotherUser(){
        val testUserEmail = "learqa20230608124201@example.com"
        val testUserPassword = "1234"

        val authData = HashMap<String, String>()
        authData["email"] = testUserEmail
        authData["password"] = testUserPassword

        val response =  apiCoreRequests.makePostRequest(
            "https://playground.learnqa.ru/api/user/login",
            authData
        )

        val cookie = getCookie(response, "auth_sid")
        val header = getHeader(response, "x-csrf-token")

        val testExistingUserId = 72380

        val responseDelete = apiCoreRequests
            .makeDeleteRequest(
                url = "https://playground.learnqa.ru/api/user/",
                id = testExistingUserId,
                token = header,
                cookie = cookie
            )

        val responseCheckDeleteUser = apiCoreRequests
            .makeGetRequest(
                url = "https://playground.learnqa.ru/api/user/$testExistingUserId",
                token = header,
                cookie = cookie
            )

        Assertions.assertResponseHasKey(responseCheckDeleteUser, "username")
    }
}