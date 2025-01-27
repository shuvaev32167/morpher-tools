package ru.shuvaev.morpher.tools.type

import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender

/** Общий интерфейс для всех движков морфера */
interface MorpherType {
    /**
     * Преобразование существительного
     *
     * @param word слово для преобразования
     * @param gender род слова
     * @param case в какой падеж надо преобразовать
     * @param autoGender надо ли попытаться автоматически определить род слова
     *    [word]
     * @return Преобразованное слово, или, если не удалось, исходное [word]
     */
    fun morphNoun(
        word: String,
        gender: Gender = Gender.MALE,
        case: Case = Case.DATIVUS,
        autoGender: Boolean = true
    ): String

    fun morphNoun(word: String): String = morphNoun(word, autoGender = true)
}