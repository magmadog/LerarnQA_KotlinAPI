import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class HelloWorldTest() {

    @Test
    fun testRestAssured(){

        val params = mutableMapOf<String, String>()
        params["name"] = "John"

        val response = RestAssured
            .given()
            .params(params)
            .get("https://playground.learnqa.ru/api/hello")
            .jsonPath()
        val answer = response.get<String>("answer")
        if (answer == null)
            println("The key 'answer' is null")
        else
            println(answer)
    }

    @Test
    fun testCheckType(){

        val body = HashMap<String, String>()
        body["param1"] = "value1"
        body["param2"] = "value2"

        val response = RestAssured
            .given()
            .body(body)
            .post("https://playground.learnqa.ru/api/check_type")
            .andReturn()

        println("Status code: ${response.statusCode}")
        response.print()
    }

    @Test
    fun testHeaders(){
        val headers = HashMap<String, String>()
        headers["myHeader1"] = "myValue1"
        headers["myHeader2"] = "myValue2"

        val response = RestAssured
            .given()
            .headers(headers)
            .get("https://playground.learnqa.ru/api/show_all_headers")
            .andReturn()

        response.prettyPrint()

        val responseHeaders = response.headers
        print(responseHeaders)
    }

    @Test
    fun testCookies(){
        val data = HashMap<String, String>()
        data["login"] = "secret_login"
        data["password"] = "secret_pass"

        val responseForGetAuthCookie = RestAssured
            .given()
            .body(data)
            .get("https://playground.learnqa.ru/api/get_auth_cookie")
            .andReturn()

        val responseAuthCookie = responseForGetAuthCookie.cookie("auth_cookie")
        if (responseAuthCookie == null){
            println("Can't find auth cookies")
            return
        }

        val cookies = HashMap<String, String>()
        cookies["auth_cookie"] = responseAuthCookie

        val responseForAuth = RestAssured
            .given()
            .cookies(cookies)
            .get("https://playground.learnqa.ru/api/check_auth_cookie")
            .andReturn()

        responseForAuth.print()
    }
}