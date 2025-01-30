package ru.shuvaev.morpher.tools.cache.data

import ru.morpher.ws3.russian.DeclensionResult
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration
import java.sql.ResultSet

data class MorphNameDto(
    val mascNominativus: String?,
    val mascGenitivus: String?,
    val mascDativus: String?,
    val mascAccusativus: String?,
    val mascInstrumentalis: String?,
    val mascPraepositionalis: String?,
    val femNominativus: String?,
    val femGenitivus: String?,
    val femDativus: String?,
    val femAccusativus: String?,
    val femInstrumentalis: String?,
    val femPraepositionalis: String?,
    val pluralNominativus: String?,
    val pluralGenitivus: String?,
    val pluralDativus: String?,
    val pluralAccusativus: String?,
    val pluralInstrumentalis: String?,
    val pluralPraepositionalis: String?,
) {
    companion object {
        fun fromWs3Morpher(data: DeclensionResult?, gender: Gender): MorphNameDto? {
            if (data == null) return null

            return when (gender) {
                Gender.MALE -> MorphNameDto(
                    data.nominative,
                    data.genitive,
                    data.dative,
                    data.accusative,
                    data.instrumental,
                    data.prepositional,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    data.plural?.nominative,
                    data.plural?.genitive,
                    data.plural?.dative,
                    data.plural?.accusative,
                    data.plural?.instrumental,
                    data.plural?.prepositional
                )

                Gender.FEMALE -> TODO()
                Gender.MEDIUM -> null
            }
        }

        @JvmStatic
        fun createFromResultSet(rs: ResultSet): MorphNameDto {
            return MorphNameDto(
                rs.getString("masc_nominativus"),
                rs.getString("masc_genitivus"),
                rs.getString("masc_dativus"),
                rs.getString("masc_accusativus"),
                rs.getString("masc_instrumentalis"),
                rs.getString("masc_praepositionalis"),
                rs.getString("fem_nominativus"),
                rs.getString("fem_genitivus"),
                rs.getString("fem_dativus"),
                rs.getString("fem_accusativus"),
                rs.getString("fem_instrumentalis"),
                rs.getString("fem_praepositionalis"),
                rs.getString("plural_nominativus"),
                rs.getString("plural_genitivus"),
                rs.getString("plural_dativus"),
                rs.getString("plural_accusativus"),
                rs.getString("plural_instrumentalis"),
                rs.getString("plural_praepositionalis"),
            )
        }
    }

    fun getMorph(case: Case, gender: Gender, numeration: Numeration): String? {
        return when (numeration) {
            Numeration.PLURAL -> when (case) {
                Case.NOMINATIVUS -> pluralNominativus
                Case.GENITIVUS -> pluralGenitivus
                Case.DATIVUS -> pluralDativus
                Case.ACCUSATIVUS -> pluralAccusativus
                Case.INSTRUMENTALIS -> pluralInstrumentalis
                Case.PRAEPOSITIONALIS -> pluralPraepositionalis
            }

            Numeration.SINGLE -> when (gender) {
                Gender.MALE -> when (case) {
                    Case.NOMINATIVUS -> mascNominativus
                    Case.GENITIVUS -> mascGenitivus
                    Case.DATIVUS -> mascDativus
                    Case.ACCUSATIVUS -> mascAccusativus
                    Case.INSTRUMENTALIS -> mascInstrumentalis
                    Case.PRAEPOSITIONALIS -> mascPraepositionalis
                }

                Gender.FEMALE -> when (case) {
                    Case.NOMINATIVUS -> femNominativus
                    Case.GENITIVUS -> femGenitivus
                    Case.DATIVUS -> femDativus
                    Case.ACCUSATIVUS -> femAccusativus
                    Case.INSTRUMENTALIS -> femInstrumentalis
                    Case.PRAEPOSITIONALIS -> femPraepositionalis
                }

                Gender.MEDIUM -> null
            }
        }
    }
}
