package ru.shuvaev.morpher.tools.cache

import ru.shuvaev.morpher.tools.cache.data.MorphGenderDto
import ru.shuvaev.morpher.tools.cache.data.MorphologyDto
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


internal object SqlLiteCache : Cache {
    override fun getMorphedNoun(word: String): MorphologyDto? {
        try {
            return baseDbAction { statement ->
                val rs = statement.executeQuery("select * from noun where nominativus='${word}';")
                return@baseDbAction if (rs.next()) {
                    MorphologyDto(
                        rs.getString("nominativus"),
                        rs.getString("genitivus"),
                        rs.getString("dativus"),
                        rs.getString("accusativus"),
                        rs.getString("instrumentalis"),
                        rs.getString("praepositionalis"),
                        rs.getString("plural_nominativus"),
                        rs.getString("plural_genitivus"),
                        rs.getString("plural_dativus"),
                        rs.getString("plural_accusativus"),
                        rs.getString("plural_instrumentalis"),
                        rs.getString("plural_praepositionalistext")
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    override fun saveMorphedNoun(data: MorphologyDto): MorphologyDto {
        try {
            return baseDbAction { statement ->
                statement.executeUpdate(
                    "insert into noun (nominativus, genitivus, dativus, accusativus," +
                            "instrumentalis, praepositionalis, plural_nominativus, plural_genitivus, " +
                            "plural_dativus, plural_accusativus, plural_instrumentalis, plural_praepositionalistext) " +
                            "values ('${data.nominativus}','${data.genitivus}','${data.dativus}'," +
                            "'${data.accusativus}','${data.instrumentalis}','${data.praepositionalis}'," +
                            "${stringOrNull(data.pluralNominativus)},${stringOrNull(data.pluralGenitivus)}," +
                            "${stringOrNull(data.pluralDativus)},${stringOrNull(data.pluralAccusativus)}," +
                            "${stringOrNull(data.pluralInstrumentalis)},${stringOrNull(data.pluralPraepositionalis)}) " +
                            "on conflict do nothing;"
                )
                return@baseDbAction data
            } ?: data
        } catch (e: Exception) {
            println(e)
        }
        return data
    }

    override fun getMorphedGender(word: String): MorphGenderDto? {
        try {
            return baseDbAction {
                val rs = it.executeQuery(
                    "select * from adjective_genders where masculine='${word}' or feminine='${word}' " +
                            "or neuter='${word}';"
                )
                return@baseDbAction if (rs.next()) {
                    MorphGenderDto(
                        rs.getString("masculine"),
                        rs.getString("feminine"),
                        rs.getString("neuter"),
                        rs.getString("plural")
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    override fun saveMorphedGender(data: MorphGenderDto): MorphGenderDto {
        try {
            return baseDbAction {
                it.executeUpdate(
                    "insert into adjective_genders (masculine, feminine, neuter, plural) " +
                            "values ('${data.masculine}', '${data.feminine}', '${data.neuter}', '${data.plural}') " +
                            "on conflict do nothing;"
                )
                return@baseDbAction data
            } ?: data
        } catch (e: Exception) {
            println(e)
        }
        return data
    }

    private fun createTables(statement: Statement) {
        statement.queryTimeout = 30
        statement.executeUpdate(
            "create table if not exists noun(" +
                    "nominativus text primary key not null, " +
                    "genitivus text not null, " +
                    "dativus text not null, " +
                    "accusativus text not null, " +
                    "instrumentalis text not null, " +
                    "praepositionalis text not null, " +
                    "plural_nominativus text, " +
                    "plural_genitivus text, " +
                    "plural_dativus text, " +
                    "plural_accusativus text, " +
                    "plural_instrumentalis text, " +
                    "plural_praepositionalistext);"
        )

        statement.executeUpdate(
            "create table if not exists adjective_genders(" +
                    "masculine text primary key," +
                    "feminine text, " +
                    "neuter text," +
                    "plural text, " +
                    "unique(feminine), " +
                    "unique(neuter));"
        )
    }

    @JvmStatic
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:morpherCache.db")

    init {
        connection.let { connection ->
            connection.createStatement().use { statement ->
                createTables(statement)
            }
        }
    }

    private fun <T> baseDbAction(block: (statement: Statement) -> T): T? {
        return connection.let { connection ->
            connection.createStatement().use { statement ->
                return block(statement)
            }
        }
    }

    private fun stringOrNull(string: String?): String? {
        return if (string == null) {
            null
        } else
            "'$string'"
    }
}