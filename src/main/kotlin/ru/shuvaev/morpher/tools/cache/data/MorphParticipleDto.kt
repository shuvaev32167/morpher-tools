package ru.shuvaev.morpher.tools.cache.data

import java.sql.ResultSet

data class MorphParticipleDto(
    var id: Int? = null,
    var fullMasc: String? = null,
    var fullFem: String? = null,
    var fullNeut: String? = null,
    var fullPlur: String? = null,
    var shortMasc: String? = null,
    var shortFem: String? = null,
    var shortNeut: String? = null,
    var shortPlur: String? = null,
) {
    companion object {
        @JvmStatic
        internal fun createFromResultSet(rs: ResultSet): MorphParticipleDto {
            return MorphParticipleDto(
                rs.getInt("id"),
                rs.getString("full_masculine"),
                rs.getString("full_feminine"),
                rs.getString("full_neuter"),
                rs.getString("full_plural"),
                rs.getString("short_masculine"),
                rs.getString("short_feminine"),
                rs.getString("short_neuter"),
                rs.getString("short_plural")
            )
        }
    }
}
