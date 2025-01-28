package ru.shuvaev.morpher.tools.cache

import ru.shuvaev.morpher.tools.cache.data.MorphologyDto
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


object SqlLiteCache : Cache {
    override fun getMorphed(word: String): MorphologyDto? {
        try {
            return baseDbAction { statement ->
                createTable(statement)
                val rs = statement.executeQuery("select * from word where nominativus='${word}';")
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

    override fun saveMorphed(morphologyDto: MorphologyDto): MorphologyDto {
        try {
            return baseDbAction { statement ->
                statement.executeUpdate(
                    "insert into word (nominativus, genitivus, dativus, accusativus," +
                            "instrumentalis, praepositionalis, plural_nominativus, plural_genitivus, " +
                            "plural_dativus, plural_accusativus, plural_instrumentalis, plural_praepositionalistext) " +
                            "values ('${morphologyDto.nominativus}','${morphologyDto.genitivus}','${morphologyDto.dativus}'," +
                            "'${morphologyDto.accusativus}','${morphologyDto.instrumentalis}','${morphologyDto.praepositionalis}'," +
                            "'${morphologyDto.pluralNominativus}','${morphologyDto.pluralGenitivus}'," +
                            "'${morphologyDto.pluralDativus}','${morphologyDto.pluralAccusativus}'," +
                            "'${morphologyDto.pluralInstrumentalis}','${morphologyDto.pluralPraepositionalis}') " +
                            "on conflict do nothing;"
                )
                return@baseDbAction morphologyDto
            } ?: morphologyDto
        } catch (e: Exception) {
            println(e)
        }
        return morphologyDto
    }

    private fun createTable(statement: Statement) {
        statement.queryTimeout = 30
        statement.executeUpdate(
            "create table if not exists word(" +
                    "nominativus text primary key, " +
                    "genitivus text, " +
                    "dativus text, " +
                    "accusativus text, " +
                    "instrumentalis text, " +
                    "praepositionalis text, " +
                    "plural_nominativus text, " +
                    "plural_genitivus text, " +
                    "plural_dativus text, " +
                    "plural_accusativus text, " +
                    "plural_instrumentalis text, " +
                    "plural_praepositionalistext);"
        )
    }

    private fun <T> baseDbAction(block: (statement: Statement) -> T): T? {
        val connection: Connection? = DriverManager.getConnection("jdbc:sqlite:morpherCache.db")
        if (connection == null) {
            return null
        }
        return connection.use { connection ->
            connection.createStatement().use { statement ->
                createTable(statement)
                return block(statement)
            }
        }
    }
}