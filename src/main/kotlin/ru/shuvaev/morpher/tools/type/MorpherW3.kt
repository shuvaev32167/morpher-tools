package ru.shuvaev.morpher.tools.type

import ru.shuvaev.morpher.tools.cache.SqlLiteCache
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
        val morphed = SqlLiteCache.getMorphed(word)
        return morphed?.getMorph(case, numeration)
            ?: (RUSSIAN_CLIENT.declension(word).let {
                MorphologyDto.fromWs3Morpher(it)?.let {
                    SqlLiteCache.saveMorphed(it)
                }
            }?.getMorph(case, numeration) ?: word)
    }
}