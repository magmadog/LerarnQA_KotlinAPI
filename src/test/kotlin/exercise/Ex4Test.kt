package exercise

import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Ex4Test {

    @Test
    fun testGetText(){
        val response = RestAssured
            .get(" https://playground.learnqa.ru/api/get_text")
            .andReturn()
        response.prettyPrint()
    }
}