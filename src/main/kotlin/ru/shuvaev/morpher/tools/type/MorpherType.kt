package ru.shuvaev.morpher.tools.type

import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.enams.Gender
import ru.shuvaev.morpher.tools.enams.Numeration
import kotlin.math.abs

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

    fun morphGender(word: String, gender: Gender, numeration: Numeration): String
    fun morphGender(word: String, gender: Gender): String =
        morphGender(word = word, gender = gender, numeration = Numeration.SINGLE)

    fun morphFirstName(
        firstName: String,
        case: Case,
        gender: Gender = Gender.MALE,
        numeration: Numeration = Numeration.SINGLE
    ): String {
        return morphNoun(firstName, gender, case, numeration, false)
    }

    fun morphLastName(
        lastName: String,
        case: Case,
        gender: Gender = Gender.MALE,
        numeration: Numeration = Numeration.SINGLE
    ): String {
        return morphNoun(lastName, gender, case, numeration, false)
    }

    /**
     * Преобразование мужской фамилии в женскую
     *
     * @param surname мужская фамилия
     * @return [surname] в женском варианте
     */
    fun convertSurnameToFemale(surname: String): String {
        val parts = surname.split("-") // Проверка на двойные фамилии
        val convertedParts = parts.map { processSingleSurname(it) }
        return convertedParts.joinToString("-") // Объединяем обратно
    }

    private fun processSingleSurname(surname: String): String {
        return when {
            // Фамилии на "-ов", "-ев", "-ин", "-ын" → добавляем "-а"
            surname.matches(Regex(".*(ов|ев|ин|ын)$")) -> surname + "а"

            // Фамилии на "-ский", "-цкий", "-чий", "-ой", "-ый" → заменяем на "-ая"
            surname.endsWith("ский") || surname.endsWith("цкий") || surname.endsWith("чий")
                    || surname.endsWith("ой") || surname.endsWith("ый") || surname.endsWith("ай") -> surname.dropLast(2) + "ая"

            // Фамилии на "-арь" → заменяем на "-арова" (Гончар → Гончарова)
            surname.endsWith("арь") -> surname + "ова"

            // Фамилии на "-ец" → заменяем на "-ецкая" (Князец → Князецкая)
            surname.endsWith("ец") -> surname + "кая"

            // Фамилии на "-эй", "-ий" → заменяем на "-эя", "-ия"
            surname.endsWith("эй") || surname.endsWith("ий") -> surname.dropLast(1) + "я"

            else -> surname // По умолчанию фамилия остается неизменной
        }
    }

    /**
     * Преобразование счётного существительного
     *
     * @param count числительное, соединённое со счётным существительным [noun]
     * @param noun счётное числительное, соединённое с числительным [count]
     * @param case падеж, в которое надо преобразовать
     * @return преобразованное счётное числительное [noun], в соответствии с числительным [count], в требуемом падеже [case]
     */
    fun morphCountableNoun(count: Int, noun: String, case: Case = Case.NOMINATIVUS): String {
        return if (case == Case.NOMINATIVUS) {
            val n = abs(count) // Обрабатываем отрицательные числа
            val mod100 = n % 100
            val mod10 = n % 10
            when {
                mod100 in 5..20 -> morphNoun(noun, Case.GENITIVUS, Numeration.PLURAL)
                mod10 == 1 -> noun
                mod10 in 2..4 -> morphNoun(noun, Case.GENITIVUS)
                else -> morphNoun(noun, Case.GENITIVUS, Numeration.PLURAL)
            }
        } else {
            if (count == 1) {
                morphNoun(noun, case)
            } else {
                morphNoun(noun, case, Numeration.PLURAL)
            }
        }
    }

    /**
     * Преобразование счётного существительного
     *
     * @param count числительное, соединённое со счётным существительным [noun]
     * @param noun счётное числительное, соединённое с числительным [count]
     * @param case падеж, в которое надо преобразовать
     * @return преобразованное счётное числительное [noun], в соответствии с числительным [count], в требуемом падеже [case]
     */
    fun morphCountableNoun(count: Double, noun: String, case: Case = Case.NOMINATIVUS): String {
        return if (abs(count) % 1.0 < 1e-10) {
            morphCountableNoun(count.toInt(), noun, case)
        } else {
            morphNoun(noun, Case.GENITIVUS)
        }
    }
}