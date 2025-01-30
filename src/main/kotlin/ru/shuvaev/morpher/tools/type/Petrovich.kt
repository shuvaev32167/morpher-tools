package ru.shuvaev.morpher.tools.type

import com.github.petrovich4j.NameType
import com.github.petrovich4j.Petrovich
import ru.shuvaev.morpher.tools.cache.SqlLiteCache
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration
import java.util.*

internal object Petrovich : MorpherType {
    @JvmStatic
    private val PETROVICH: Petrovich = Petrovich()

    @JvmStatic
    private val PERSONAL_PRONOUNS = mapOf(
        "я" to mapOf(
            Case.NOMINATIVUS to "я",
            Case.GENITIVUS to "меня",
            Case.DATIVUS to "мне",
            Case.ACCUSATIVUS to "меня",
            Case.INSTRUMENTALIS to "мной",
            Case.PRAEPOSITIONALIS to "мне"
        ),
        "мы" to mapOf(
            Case.NOMINATIVUS to "мы",
            Case.GENITIVUS to "нас",
            Case.DATIVUS to "нам",
            Case.ACCUSATIVUS to "нас",
            Case.INSTRUMENTALIS to "нами",
            Case.PRAEPOSITIONALIS to "нас"
        ),
        "ты" to mapOf(
            Case.NOMINATIVUS to "ты",
            Case.GENITIVUS to "тебя",
            Case.DATIVUS to "тебе",
            Case.ACCUSATIVUS to "тебя",
            Case.INSTRUMENTALIS to "тобой",
            Case.PRAEPOSITIONALIS to "тебе"
        ),
        "вы" to mapOf(
            Case.NOMINATIVUS to "вы",
            Case.GENITIVUS to "вас",
            Case.DATIVUS to "вам",
            Case.ACCUSATIVUS to "вас",
            Case.INSTRUMENTALIS to "вами",
            Case.PRAEPOSITIONALIS to "вас"
        ),
        "он" to mapOf(
            Case.NOMINATIVUS to "он",
            Case.GENITIVUS to "его",
            Case.DATIVUS to "ему",
            Case.ACCUSATIVUS to "его",
            Case.INSTRUMENTALIS to "им",
            Case.PRAEPOSITIONALIS to "нём"
        ),
        "она" to mapOf(
            Case.NOMINATIVUS to "она",
            Case.GENITIVUS to "её",
            Case.DATIVUS to "ей",
            Case.ACCUSATIVUS to "её",
            Case.INSTRUMENTALIS to "ею",
            Case.PRAEPOSITIONALIS to "ней"
        ),
        "оно" to mapOf(
            Case.NOMINATIVUS to "оно",
            Case.GENITIVUS to "его",
            Case.DATIVUS to "ему",
            Case.ACCUSATIVUS to "его",
            Case.INSTRUMENTALIS to "им",
            Case.PRAEPOSITIONALIS to "нём"
        ),
        "они" to mapOf(
            Case.NOMINATIVUS to "они",
            Case.GENITIVUS to "их",
            Case.DATIVUS to "им",
            Case.ACCUSATIVUS to "их",
            Case.INSTRUMENTALIS to "ими",
            Case.PRAEPOSITIONALIS to "них"
        )
    )

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

    override fun morphFirstName(
        firstName: String,
        case: Case,
        gender: Gender,
        numeration: Numeration
    ): String {
        if (case == Case.NOMINATIVUS) {
            return firstName
        }

        SqlLiteCache.getMorphedFirstName(firstName)?.getMorph(case, gender, numeration)?.let { return it }

        val needCapit = firstName[0].isUpperCase()
        firstName.lowercase().let {
            if (PERSONAL_PRONOUNS.containsKey(it)) {
                val result = PERSONAL_PRONOUNS[firstName.lowercase()]!![case]!!
                return if (needCapit) {
                    result.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                } else {
                    result
                }
            }
        }

        return PETROVICH.say(firstName, NameType.FirstName, convertGender(gender), convertCase(case))
    }

    override fun morphLastName(
        lastName: String,
        case: Case,
        gender: Gender,
        numeration: Numeration
    ): String {
        if (case == Case.NOMINATIVUS) {
            return lastName
        }

        SqlLiteCache.getMorphedLastName(lastName)?.getMorph(case, gender, numeration)?.let { return it }

        return PETROVICH.say(lastName, NameType.LastName, convertGender(gender), convertCase(case))
    }

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