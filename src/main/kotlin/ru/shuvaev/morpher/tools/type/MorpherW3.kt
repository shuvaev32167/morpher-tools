package ru.shuvaev.morpher.tools.type

import ru.shuvaev.morpher.tools.cache.SqlLiteCache
import ru.shuvaev.morpher.tools.cache.data.MorphGenderDto
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
            val morphed = SqlLiteCache.getMorphedNoun(word)
            return morphed?.getMorph(case, numeration)
                ?: (RUSSIAN_CLIENT.declension(word).let {
                    MorphologyDto.fromWs3Morpher(it)?.let {
                        SqlLiteCache.saveMorphedNoun(it)
                    }
                }?.getMorph(case, numeration) ?: word)
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
        val morphed = SqlLiteCache.getMorphedGender(word)
        if (morphed != null) {
            return morphed.getMorph(gender, numeration)
        }
        try {
            return RUSSIAN_CLIENT.adjectiveGenders(word).let {
                MorphGenderDto.fromWs3Morpher(word, it)?.let {
                    SqlLiteCache.saveMorphedGender(it)
                }
            }?.getMorph(gender, numeration) ?: word
        } catch (e: Exception) {
            println(e)
        }
        return word
    }
}