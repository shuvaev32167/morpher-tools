package ru.shuvaev.morpher.tools.cache

import ru.shuvaev.morpher.tools.cache.data.MorphGenderDto
import ru.shuvaev.morpher.tools.cache.data.MorphNameDto
import ru.shuvaev.morpher.tools.cache.data.MorphologyDto
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement


internal object SqlLiteCache : Cache {
    override fun getMorphedNoun(word: String): List<MorphologyDto> {
        try {
            return baseDbAction { statement ->
                val rs =
                    statement.executeQuery("select * from noun where nominativus='${word}' or plural_nominativus='${word}';")

                val result = mutableListOf<MorphologyDto>()

                while (rs.next()) {
                    result.add(MorphologyDto.createFromResultSet(rs))
                }

                return@baseDbAction result
            } ?: emptyList<MorphologyDto>()
        } catch (e: Exception) {
            println(e)
        }
        return emptyList<MorphologyDto>()
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

    override fun getMorphedGender(word: String): List<MorphGenderDto> {
        try {
            return baseDbAction {
                val rs = it.executeQuery(
                    "select * from adjective_genders where masculine='${word}' or feminine='${word}' " +
                            "or neuter='${word}' or plural='$word';"
                )

                val result = mutableListOf<MorphGenderDto>()

                while (rs.next()) {
                    result.add(MorphGenderDto.createFromResultSet(rs))
                }

                return@baseDbAction result
            } ?: emptyList<MorphGenderDto>()
        } catch (e: Exception) {
            println(e)
        }
        return emptyList<MorphGenderDto>()
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

    override fun getMorphedFirstName(word: String): MorphNameDto? {
        try {
            return baseDbAction {
                val rs = it.executeQuery(
                    "select * from first_name where masc_nominativus='${word}' or fem_nominativus='${word}' " +
                            "or plural_nominativus='${word}';"
                )
                return@baseDbAction if (rs.next()) {
                    MorphNameDto.createFromResultSet(rs)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    override fun saveMorphedFirstName(data: MorphNameDto): MorphNameDto {
        try {
            return baseDbAction {
                it.executeUpdate(
                    "insert into first_name (" +
                            "masc_nominativus, masc_genitivus, masc_dativus, masc_accusativus, masc_instrumentalis, masc_praepositionalis, " +
                            "fem_nominativus, fem_genitivus, fem_dativus, fem_accusativus, fem_instrumentalis, fem_praepositionalis, " +
                            "plural_nominativus, plural_genitivus, plural_dativus, plural_accusativus, plural_instrumentalis, plural_praepositionalis) " +
                            "values (" +
                            "${stringOrNull(data.mascNominativus)},${stringOrNull(data.mascGenitivus)},${
                                stringOrNull(
                                    data.mascDativus
                                )
                            },${stringOrNull(data.mascAccusativus)},${stringOrNull(data.mascInstrumentalis)},${
                                stringOrNull(
                                    data.mascPraepositionalis
                                )
                            }," +
                            "${stringOrNull(data.femNominativus)},${stringOrNull(data.femGenitivus)},${stringOrNull(data.femDativus)},${
                                stringOrNull(
                                    data.femAccusativus
                                )
                            },${stringOrNull(data.femInstrumentalis)},${stringOrNull(data.femPraepositionalis)}," +
                            "${stringOrNull(data.pluralNominativus)},${stringOrNull(data.pluralGenitivus)},${
                                stringOrNull(
                                    data.pluralDativus
                                )
                            },${stringOrNull(data.pluralAccusativus)},${stringOrNull(data.pluralInstrumentalis)},${
                                stringOrNull(
                                    data.pluralPraepositionalis
                                )
                            }) " +
                            "on conflict do nothing;"
                )
                return@baseDbAction data
            } ?: data
        } catch (e: Exception) {
            println(e)
        }
        return data
    }

    override fun getMorphedLastName(word: String): MorphNameDto? {
        try {
            return baseDbAction {
                val rs = it.executeQuery(
                    "select * from last_name where masc_nominativus='${word}' or fem_nominativus='${word}' " +
                            "or plural_nominativus='${word}';"
                )
                return@baseDbAction if (rs.next()) {
                    MorphNameDto.createFromResultSet(rs)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println(e)
        }
        return null
    }

    override fun saveMorphedLastName(data: MorphNameDto): MorphNameDto {
        try {
            return baseDbAction {
                it.executeUpdate(
                    "insert into last_name (" +
                            "masc_nominativus, masc_genitivus, masc_dativus, masc_accusativus, masc_instrumentalis, masc_praepositionalis, " +
                            "fem_nominativus, fem_genitivus, fem_dativus, fem_accusativus, fem_instrumentalis, fem_praepositionalis, " +
                            "plural_nominativus, plural_genitivus, plural_dativus, plural_accusativus, plural_instrumentalis, plural_praepositionalis) " +
                            "values (" +
                            "${stringOrNull(data.mascNominativus)},${stringOrNull(data.mascGenitivus)},${
                                stringOrNull(
                                    data.mascDativus
                                )
                            },${stringOrNull(data.mascAccusativus)},${stringOrNull(data.mascInstrumentalis)},${
                                stringOrNull(
                                    data.mascPraepositionalis
                                )
                            }," +
                            "${stringOrNull(data.femNominativus)},${stringOrNull(data.femGenitivus)},${stringOrNull(data.femDativus)},${
                                stringOrNull(
                                    data.femAccusativus
                                )
                            },${stringOrNull(data.femInstrumentalis)},${stringOrNull(data.femPraepositionalis)}," +
                            "${stringOrNull(data.pluralNominativus)},${stringOrNull(data.pluralGenitivus)},${
                                stringOrNull(
                                    data.pluralDativus
                                )
                            },${stringOrNull(data.pluralAccusativus)},${stringOrNull(data.pluralInstrumentalis)},${
                                stringOrNull(
                                    data.pluralPraepositionalis
                                )
                            }) " +
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
                    "unique(neuter));"
        )

        statement.executeUpdate(
            "create table if not exists first_name(" +
                    "masc_nominativus text unique," +
                    "masc_genitivus text," +
                    "masc_dativus text," +
                    "masc_accusativus text," +
                    "masc_instrumentalis text," +
                    "masc_praepositionalis text," +
                    "fem_nominativus text unique," +
                    "fem_genitivus text," +
                    "fem_dativus text," +
                    "fem_accusativus text," +
                    "fem_instrumentalis text," +
                    "fem_praepositionalis text," +
                    "plural_nominativus text," +
                    "plural_genitivus text," +
                    "plural_dativus text," +
                    "plural_accusativus text," +
                    "plural_instrumentalis text," +
                    "plural_praepositionalis text);"
        )

        statement.executeUpdate(
            "create table if not exists last_name(" +
                    "masc_nominativus text unique," +
                    "masc_genitivus text," +
                    "masc_dativus text," +
                    "masc_accusativus text," +
                    "masc_instrumentalis text," +
                    "masc_praepositionalis text," +
                    "fem_nominativus text unique," +
                    "fem_genitivus text," +
                    "fem_dativus text," +
                    "fem_accusativus text," +
                    "fem_instrumentalis text," +
                    "fem_praepositionalis text," +
                    "plural_nominativus text," +
                    "plural_genitivus text," +
                    "plural_dativus text," +
                    "plural_accusativus text," +
                    "plural_instrumentalis text," +
                    "plural_praepositionalis text);"
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