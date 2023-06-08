package lib

import io.qameta.allure.Step
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured.given
import io.restassured.http.Header
import io.restassured.response.Response

class ApiCoreRequests {

    @Step("Make a GET request with token and auth cookie")
    fun makeGetRequest(url: String, token: String, cookie: String): Response{
        return given()
            .filter(AllureRestAssured())
            .header(Header("x-csrf-token", token))
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn()
    }

    @Step("Make a GET request with cookie")
    fun makeGetRequestWithCookie(url: String, cookie: String): Response{
        return given()
            .filter(AllureRestAssured())
            .cookie("auth_sid", cookie)
            .get(url)
            .andReturn()
    }

    @Step("Make a GET request with token")
    fun makeGetRequestWithToken(url: String, token: String): Response{
        return given()
            .filter(AllureRestAssured())
            .header(Header("x-csrf-token", token))
            .get(url)
            .andReturn()
    }

    @Step("Make a POST request")
    fun makePostRequest(url: String, authData: Map<String, String>): Response{
        return given()
            .filter(AllureRestAssured())
            .body(authData)
            .post(url)
            .andReturn()
    }

    @Step("Make a PUT request for Edit user with id: {id}")
    fun makePutRequest(url: String, token: String, cookie: String, newData: Map<String, String>, id: Int): Response{
        return given()
            .filter(AllureRestAssured())
            .header("x-csrf-token", token)
            .cookie("auth_sid", cookie)
            .body(newData)
            .put(url+id)
            .andReturn()
    }
}