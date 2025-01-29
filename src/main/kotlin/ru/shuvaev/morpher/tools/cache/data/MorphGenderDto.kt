package ru.shuvaev.morpher.tools.cache.data

import ru.morpher.ws3.russian.AdjectiveGendersResult
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration

data class MorphGenderDto(val masculine: String, val feminine: String, val neuter: String, val plural: String) {
    companion object {
        @JvmStatic
        fun fromWs3Morpher(masculine: String, data: AdjectiveGendersResult?): MorphGenderDto? {
            if (data == null) return null

            return MorphGenderDto(masculine, data.feminine, data.neuter, data.plural)
        }
    }

    fun getMorph(gender: Gender, numeration: Numeration): String {
        if (numeration == Numeration.PLURAL) {
            return plural
        }
        return when (gender) {
            Gender.MALE -> masculine
            Gender.FEMALE -> feminine
            Gender.MEDIUM -> neuter
        }
    }
}
