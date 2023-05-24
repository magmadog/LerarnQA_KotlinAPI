import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Ex8Token {

    @Test
    fun test(){
        val url = "https://playground.learnqa.ru/ajax/api/longtime_job"

        val responseForGetTokenAndSeconds = RestAssured
            .get(url)
            .jsonPath()
        responseForGetTokenAndSeconds.prettyPrint()

        val token = responseForGetTokenAndSeconds.get<String>("token")
        val seconds = responseForGetTokenAndSeconds.get<Int>("seconds")

        // 1. Task start
        val responseForStartTask = RestAssured
            .given()
            .param("token", token)
            .get(url)
            .andReturn()

        // 2. Request for status check
        val responseForCheckStatus = RestAssured
            .given()
            .param("token", token)
            .get(url)
            .andReturn()
        responseForCheckStatus.print()

        // 3. Wait few seconds
        Thread.sleep(seconds * 1000.toLong())

        // 4. Check result status
        val responseForCheckResultStatus = RestAssured
            .given()
            .param("token", token)
            .get(url)
            .andReturn()
        responseForCheckResultStatus.print()
    }
}