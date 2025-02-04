package ru.shuvaev.morpher.tools.type

import ru.morpher.ws3.russian.DeclensionFlag
import ru.shuvaev.morpher.tools.cache.SqlLiteCache
import ru.shuvaev.morpher.tools.cache.data.MorphGenderDto
import ru.shuvaev.morpher.tools.cache.data.MorphNameDto
import ru.shuvaev.morpher.tools.cache.data.MorphologyDto
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration

internal object MorpherW3 : MorpherType {
    @JvmStatic
    private val RUSSIAN_CLIENT = ru.morpher.ws3.ClientBuilder().build().russian()
    override fun morphNoun(
        word: String,
        gender: Gender,
        case: Case,
        numeration: Numeration,
        autoGender: Boolean
    ): String {
        try {
            var result: String? = null
            val foundedMorphed = SqlLiteCache.getMorphedNoun(word)
            if (foundedMorphed.isNotEmpty()) {
                result = if (foundedMorphed.size == 1) {
                    foundedMorphed.first().getMorph(case, numeration)
                } else {
                    foundedMorphed.find { it.nominativus == word }
                        ?.getMorph(case, numeration)
                }
            }
            if (result != null) {
                return result
            }
            result = RUSSIAN_CLIENT.declension(word).let {
                MorphologyDto.fromWs3Morpher(it)?.let {
                    SqlLiteCache.saveMorphedNoun(it)
                }
            }?.getMorph(case, numeration)
            if (result != null) {
                return result
            }
            return word
        } catch (e: Exception) {
            println(e)
        }
        return word
    }

    override fun morphGender(
        word: String,
        gender: Gender,
        numeration: Numeration
    ): String {
        try {
            var result: String? = null
            val foundedMorphed = SqlLiteCache.getMorphedGender(word)
            if (foundedMorphed.isNotEmpty()) {
                result = if (foundedMorphed.size == 1) {
                    foundedMorphed.first().getMorph(gender, numeration)
                } else {
                    foundedMorphed.find { it.masculine == word || it.feminine == word || it.neuter == word }
                        ?.getMorph(gender, numeration)
                }
            }
            if (result != null) {
                return result
            }
            result = RUSSIAN_CLIENT.adjectiveGenders(word).let {
                MorphGenderDto.fromWs3Morpher(word, it)?.let {
                    SqlLiteCache.saveMorphedGender(it)
                }
            }?.getMorph(gender, numeration)
            if (result != null) {
                return result
            }
            return word
        } catch (e: Exception) {
            println(e)
        }
        return word
    }

    override fun morphFirstName(
        firstName: String,
        case: Case,
        gender: Gender,
        numeration: Numeration
    ): String {
        try {
            var result = SqlLiteCache.getMorphedFirstName(firstName)?.getMorph(case, gender, numeration)
            if (result != null) {
                return result
            }
            result = RUSSIAN_CLIENT.declension(firstName, DeclensionFlag.Name, genderToDeclarationFlag(gender)).let {
                MorphNameDto.fromWs3Morpher(it, gender)?.let {
                    SqlLiteCache.saveMorphedFirstName(it)
                }
            }?.getMorph(case, gender, numeration)
            if (result != null) {
                return result
            }
            return firstName
        } catch (e: Exception) {
            println(e)
        }
        return firstName
    }

    override fun morphLastName(
        lastName: String,
        case: Case,
        gender: Gender,
        numeration: Numeration
    ): String {
        return morphFirstName(lastName, case, gender, numeration)
    }

    private fun genderToDeclarationFlag(gender: Gender): DeclensionFlag {
        return when (gender) {
            Gender.FEMALE -> DeclensionFlag.Feminine
            Gender.MALE -> DeclensionFlag.Masculine
            Gender.MEDIUM -> DeclensionFlag.Neuter
        }
    }
}