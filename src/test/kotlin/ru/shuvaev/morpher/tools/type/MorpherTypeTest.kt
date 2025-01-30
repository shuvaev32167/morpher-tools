package ru.shuvaev.morpher.tools.type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration

class MorpherTypeTest {

    @Test
    fun convertSurnameToFemale() {
        val morpherType: MorpherType = object : MorpherType {
            override fun morphNoun(
                word: String,
                gender: Gender,
                case: Case,
                numeration: Numeration,
                autoGender: Boolean
            ): String {
                TODO("Not yet implemented")
            }

            override fun morphGender(
                word: String,
                gender: Gender,
                numeration: Numeration
            ): String {
                TODO("Not yet implemented")
            }

        }
        Assertions.assertEquals("Волкова", morpherType.convertSurnameToFemale("Волков"))
        Assertions.assertEquals("Лилишамарту", morpherType.convertSurnameToFemale("Лилишамарту"))
        Assertions.assertEquals("Такахаси", morpherType.convertSurnameToFemale("Такахаси"))
        Assertions.assertEquals("Янг", morpherType.convertSurnameToFemale("Янг"))
        Assertions.assertEquals("Гордон", morpherType.convertSurnameToFemale("Гордон"))
        Assertions.assertEquals("Гарднер", morpherType.convertSurnameToFemale("Гарднер"))
    }
}