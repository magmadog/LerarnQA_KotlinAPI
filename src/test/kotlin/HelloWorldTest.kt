import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class HelloWorldTest() {

    @Test
    fun testHelloWorld(){
        val response = RestAssured
            .get("https://playground.learnqa.ru/api/hello")
            .andReturn()
        response.prettyPrint()
    }
}