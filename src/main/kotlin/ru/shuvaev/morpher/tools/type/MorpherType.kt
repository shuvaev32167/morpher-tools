package ru.shuvaev.morpher.tools.type

import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration

/** Общий интерфейс для всех движков морфера */
interface MorpherType {
    /**
     * Преобразование существительного
     *
     * @param word слово для преобразования
     * @param gender род слова
     * @param case в какой падеж надо преобразовать
     * @param numeration в какое число надо преобразовать слово [word]
     * @param autoGender надо ли попытаться автоматически определить род слова
     *    [word]
     * @return Преобразованное слово, или, если не удалось, исходное [word]
     */
    fun morphNoun(
        word: String,
        gender: Gender = Gender.MALE,
        case: Case = Case.DATIVUS,
        numeration: Numeration = Numeration.SINGLE,
        autoGender: Boolean = true
    ): String

    /**
     * Преобразование существительного
     *
     * @param word слово для преобразования
     * @param case в какой падеж надо преобразовать
     * @param numeration в какое число надо преобразовать слово [word]
     * @return Преобразованное слово, или, если не удалось, исходное [word]
     */
    fun morphNoun(word: String, case: Case = Case.DATIVUS, numeration: Numeration = Numeration.SINGLE): String =
        morphNoun(word = word, case = case, numeration = numeration, autoGender = true)
}