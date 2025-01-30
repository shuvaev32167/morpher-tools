package ru.shuvaev.morpher.tools.cache.data

import ru.morpher.ws3.russian.DeclensionResult
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Numeration
import java.sql.ResultSet

data class MorphologyDto(
    val nominativus: String,
    val genitivus: String,
    val dativus: String,
    val accusativus: String,
    val instrumentalis: String,
    val praepositionalis: String,
    val pluralNominativus: String?,
    val pluralGenitivus: String?,
    val pluralDativus: String?,
    val pluralAccusativus: String?,
    val pluralInstrumentalis: String?,
    val pluralPraepositionalis: String?,
) {
    companion object {
        @JvmStatic
        internal fun fromWs3Morpher(data: DeclensionResult?): MorphologyDto? {
            if (data == null) return null

            return MorphologyDto(
                data.nominative, data.genitive, data.dative, data.accusative, data.instrumental,
                data.prepositional,
                data.plural?.nominative ?: data.nominative,
                data.plural?.genitive ?: data.genitive,
                data.plural?.dative ?: data.dative,
                data.plural?.accusative ?: data.accusative,
                data.plural?.instrumental ?: data.instrumental,
                data.plural?.prepositional ?: data.prepositional,
            )
        }

        @JvmStatic
        internal fun createFromResultSet(rs: ResultSet): MorphologyDto {
            return MorphologyDto(
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
        }
    }

    fun getMorph(case: Case, numeration: Numeration): String {
        return when (numeration) {
            Numeration.SINGLE -> when (case) {
                Case.NOMINATIVUS -> nominativus
                Case.GENITIVUS -> genitivus
                Case.DATIVUS -> dativus
                Case.ACCUSATIVUS -> accusativus
                Case.INSTRUMENTALIS -> instrumentalis
                Case.PRAEPOSITIONALIS -> praepositionalis
            }

            Numeration.PLURAL -> when (case) {
                Case.NOMINATIVUS -> pluralNominativus
                Case.GENITIVUS -> pluralGenitivus
                Case.DATIVUS -> pluralDativus
                Case.ACCUSATIVUS -> pluralAccusativus
                Case.INSTRUMENTALIS -> pluralInstrumentalis
                Case.PRAEPOSITIONALIS -> pluralPraepositionalis
            } ?: getMorph(case, Numeration.SINGLE)
        }
    }
}
