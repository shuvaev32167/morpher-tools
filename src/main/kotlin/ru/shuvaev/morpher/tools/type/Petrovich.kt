package ru.shuvaev.morpher.tools.type

import com.github.petrovich4j.NameType
import com.github.petrovich4j.Petrovich
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration

internal object Petrovich : MorpherType {
    @JvmStatic
    private val PETROVICH: Petrovich = Petrovich()
    override fun morphNoun(
        word: String,
        gender: Gender,
        case: Case,
        numeration: Numeration,
        autoGender: Boolean
    ): String =
        convertCase(case)?.let {
            PETROVICH.say(word, NameType.FirstName, autoGender(word, gender, autoGender), it)
        } ?: word

    override fun morphGender(
        word: String,
        gender: Gender,
        numeration: Numeration
    ): String {
        TODO("Not yet implemented")
    }

    private fun autoGender(word: String, gender: Gender, autoGender: Boolean): com.github.petrovich4j.Gender {
        return if (autoGender) {
            PETROVICH.gender(word, convertGender(gender))
        } else {
            convertGender(gender)
        }
    }

    private fun convertGender(gender: Gender): com.github.petrovich4j.Gender {
        return when (gender) {
            Gender.MALE -> com.github.petrovich4j.Gender.Male
            Gender.FEMALE -> com.github.petrovich4j.Gender.Female
            Gender.MEDIUM -> com.github.petrovich4j.Gender.Both
        }
    }
    private fun convertCase(case: Case): com.github.petrovich4j.Case? {
        return when (case) {
            Case.NOMINATIVUS -> null
            Case.GENITIVUS -> com.github.petrovich4j.Case.Genitive
            Case.DATIVUS -> com.github.petrovich4j.Case.Dative
            Case.ACCUSATIVUS -> com.github.petrovich4j.Case.Accusative
            Case.INSTRUMENTALIS -> com.github.petrovich4j.Case.Instrumental
            Case.PRAEPOSITIONALIS -> com.github.petrovich4j.Case.Prepositional
        }
    }
}