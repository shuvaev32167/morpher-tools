package ru.shuvaev.morpher.tools.type

import com.github.petrovich4j.Gender
import com.github.petrovich4j.NameType
import ru.shuvaev.morpher.tools.enams.Case

object Petrovich : MorpherType {
    @JvmStatic
    private val PETROVICH: com.github.petrovich4j.Petrovich = com.github.petrovich4j.Petrovich()
    override fun morphNoun(word: String, isFemale: Boolean, case: Case, autoGender: Boolean): String = convertCase(case)?.let {
        PETROVICH.say(word, NameType.FirstName, autoGender(word, isFemale, autoGender), it)
    } ?: ""

    private fun autoGender(word: String, isFemale: Boolean, autoGender: Boolean): Gender {
        return if (autoGender) {
            PETROVICH.gender(word, convertGender(isFemale))
        } else {
            convertGender(isFemale)
        }
    }
    private fun convertGender(isFemale: Boolean): Gender = if (isFemale) Gender.Female else Gender.Male
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