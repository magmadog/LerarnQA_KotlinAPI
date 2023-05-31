package lib

import io.restassured.response.Response
import org.hamcrest.Matchers.hasKey
import kotlin.test.assertTrue

open class BaseTestCase {

    fun getHeader(response: Response, name: String): String{
        val headers = response.headers

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name: $name")
        return headers.getValue(name)
    }

    fun getCookie(response: Response, name: String): String{
        val cookies = response.cookies

        assertTrue(cookies.containsKey(name), "Response doesn't have cookie with name: $name")
        return cookies.getValue(name)
    }

    fun getIntFromJson(response: Response, name: String): Int{
        response.then().assertThat().body("$", hasKey(name))
        return response.jsonPath().getInt(name)
    }
}