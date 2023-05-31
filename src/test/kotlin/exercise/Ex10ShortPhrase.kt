package exercise

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class Ex10ShortPhrase {

    @Test
    fun testPhraseGreaterThan15(){
        val testPhrase = "0123456789123456"

        assertTrue(
            testPhrase.length > 15,
            "Phrase shorter than 15 characters"
        )
    }
}