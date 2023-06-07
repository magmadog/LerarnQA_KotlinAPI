package lib

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object DataGenerator {

    fun getRandomEmail(): String{
        val timestamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
        return "learqa$timestamp@example.com"
    }

    fun getRegistrationData(): HashMap<String, String>{
        val data = HashMap<String, String>()
        data["email"] = getRandomEmail()
        data["password"] = "1234"
        data["username"] = "learnqa"
        data["firstName"] = "learnqa"
        data["lastName"] = "learnqa"

        return data
    }

    fun getRegistrationData(notDefaultValues: Map<String, String>): HashMap<String, String>{
        val defaults = getRegistrationData()

        val userData = HashMap<String, String>()
        val fields = listOf("email", "password", "username", "firstName", "lastName")
        for (fieldName in fields){
            userData[fieldName] = notDefaultValues.getOrDefault(fieldName, defaults.getValue(fieldName))
        }

        return userData
    }
}