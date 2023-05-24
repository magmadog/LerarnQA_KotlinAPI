import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Ex7LongRedirect {

    @Test
    fun test(){
        var url: String? = "https://playground.learnqa.ru/api/long_redirect"
        var statCode = 302
        var i = 1
        while (statCode != 200) {
            println("${i++} \t Status code: $statCode \n\t Url: $url \n")

            val response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .get(url)
                .andReturn()

            if (response.headers.hasHeaderWithName("Location"))
                url = response.headers["Location"].value
            statCode = response.statusCode
        }
        /*
        Result
        1 	 Status code: 302
	         Url: https://playground.learnqa.ru/api/long_redirect

        2 	 Status code: 301
             Url: https://playground.learnqa.ru/

        3 	 Status code: 301
             Url: https://learnqa.ru/

        4 	 Status code: 301
             Url: https://www.learnqa.ru/
         */
    }
}