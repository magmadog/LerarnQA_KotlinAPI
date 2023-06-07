package lib

import io.restassured.response.Response
import org.hamcrest.Matchers.hasKey
import org.hamcrest.Matchers.not
import kotlin.test.assertEquals

object Assertions {

    fun assertJsonByName(response: Response, name: String, expectedValue: Int){
        response.then().assertThat().body("$", hasKey(name))

        val value = response.jsonPath().get<Int>(name)
        assertEquals(
            expectedValue,
            value,
            "Json value is not equals to expected value"
        )
    }

    fun assertJsonByName(response: Response, name: String, expectedValue: String){
        response.then().assertThat().body("$", hasKey(name))

        val value = response.jsonPath().get<String>(name)
        assertEquals(
            expectedValue,
            value,
            "Json value is not equals to expected value"
        )
    }

    fun assertResponseTextEquals(response: Response, expectedText: String){
        assertEquals(
            expectedText,
            response.asString(),
            "Response text is not as expected text"
        )
    }

    fun assertResponseCodeEquals(response: Response, expectedCode: Int){
        assertEquals(
            expectedCode,
            response.statusCode,
            "Response text is not as expected code"
        )
    }

    fun assertResponseHasKey(response: Response, key: String){
        response.then().assertThat().body("$", hasKey(key))
    }

    fun assertResponseHasNotKey(response: Response, key: String){
        response.then().assertThat().body("$", not(hasKey(key)))
    }

    fun assertResponseHasFields(response: Response, keys: List<String>){
        for (key in keys){
            assertResponseHasKey(response, key)
        }
    }
}