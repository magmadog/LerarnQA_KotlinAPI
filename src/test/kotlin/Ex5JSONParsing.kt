import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Ex5JSONParsing {

    @Test
    fun test(){
        val response = RestAssured
            .get("https://playground.learnqa.ru/api/get_json_homework")
            .jsonPath()

        response.prettyPrint()

        val secondMessageText = response.getMap<String, String>("messages[1]").get("message")
        println("Text of second message is: '$secondMessageText'")
    }
}