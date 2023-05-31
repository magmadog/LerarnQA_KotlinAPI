package lib

import io.restassured.response.Response
import org.hamcrest.Matchers.hasKey
import kotlin.test.assertEquals

class Assertions {

    fun assertJsonByName(response: Response, name: String, expectedValue: Int){
        response.then().assertThat().body("$", hasKey(name))

        val value = response.jsonPath().get<Int>(name)
        assertEquals(
            expectedValue,
            value,
            "Json value is not equals to expected value"
        )
    }
}