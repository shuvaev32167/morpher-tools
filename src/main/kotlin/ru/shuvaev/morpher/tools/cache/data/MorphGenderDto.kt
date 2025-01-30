package ru.shuvaev.morpher.tools.cache.data

import ru.morpher.ws3.russian.AdjectiveGendersResult
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration
import java.sql.ResultSet

data class MorphGenderDto(val masculine: String, val feminine: String, val neuter: String, val plural: String) {
    companion object {
        @JvmStatic
        internal fun fromWs3Morpher(masculine: String, data: AdjectiveGendersResult?): MorphGenderDto? {
            if (data == null) return null

            return MorphGenderDto(masculine, data.feminine, data.neuter, data.plural)
        }

        @JvmStatic
        internal fun createFromResultSet(rs: ResultSet): MorphGenderDto {
            return MorphGenderDto(
                rs.getString("masculine"),
                rs.getString("feminine"),
                rs.getString("neuter"),
                rs.getString("plural")
            )
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
