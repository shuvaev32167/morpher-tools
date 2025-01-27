package ru.shuvaev.morpher.tools.type

import ru.shuvaev.morpher.tools.enams.Case

interface MorpherType {
    fun morphNoun(word: String, isFemale: Boolean = false, case: Case = Case.DATIVUS, autoGender: Boolean = true): String
}