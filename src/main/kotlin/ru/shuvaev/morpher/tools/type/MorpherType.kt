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

    /**
     * Преобразование счётного существительного ддя характеристик
     *
     * @param count числительное, соединённое со счётным существительным [noun]
     * @param noun счётное числительное, соединённое с числительным [count]
     * @return преобразованное счётное числительное [noun], в соответствии с
     *    числительным [count]
     */
    fun morphCountableNounParam(count: Number, noun: String): String {
        val countDouble = count.toDouble()
        return if (abs(countDouble) % 1.0 < 1e-10) {
            val countLong = count.toLong()
            val n = abs(countLong) // Обрабатываем отрицательные числа
            val mod10: Short = (n % 10).toShort()
            if (mod10 == 1.toShort()) {
                noun
            } else {
                morphNoun(noun, Case.GENITIVUS)
            }
        } else {
            morphNoun(noun, Case.GENITIVUS)
        }
    }

    /**
     * Приведение причастий к каткой форме
     *
     * @param participle причастие в полной форме
     * @param gender пол
     * @param numeration число
     * @return причастие в краткой форме
     */
    fun participleToShortForm(
        participle: String,
        gender: Gender = Gender.MALE,
        numeration: Numeration = Numeration.SINGLE
    ): String? {
        return when {
            participle.endsWith("нный") -> {
                val base = participle.dropLast(3)  // Удаляем "ный"
                getShortForm(base, gender, numeration)
            }

            participle.endsWith("ый") -> {
                val base = participle.dropLast(2)  // Удаляем "ый"
                getShortForm(base, gender, numeration)
            }

            else -> participle
        }
    }

    private fun getShortForm(
        base: String,
        gender: Gender,
        numeration: Numeration,
        suffixChar: Char = '\u0000',
        additionalSuffix: String = ""
    ): String {
        val suffix = when {
            Numeration.PLURAL == numeration -> "ы"
            Gender.FEMALE == gender -> "а"
            Gender.MEDIUM == gender -> "о"
            else -> ""
        }

        return buildString {
            append(base)
            if (suffixChar != '\u0000') append(suffixChar)
            append(additionalSuffix)
            append(suffix)
        }
    }

    /**
     * Определения рода и числа существительного [noun]
     *
     * @param noun существительное, для которых надо определить род и число
     * @return Пара {Пол [Gender], Число [Numeration]}
     */
    fun determineGenderAndNumber(noun: String): Pair<Gender, Numeration> {
        val word = noun.trim().lowercase()

        // Списки исключений
        val masculineExceptions = setOf(
            "папа", "дедушка", "дядя", "кофе", "рояль", "тюль", "шампунь", "толь", "портье"
        )

        val feminineExceptions = setOf(
            "мать", "дочь", "ночь", "любовь", "болезнь", "жизнь", "тетрадь", "площадь", "степь"
        )

        val neuterExceptions = setOf(
            "время", "имя", "пламя", "знамя", "темя", "семя", "стремя", "бремя"
        )

        val pluraliaTantum = setOf(
            "ножницы", "брюки", "очки", "часы", "деньги", "ворота", "каникулы", "шахматы", "духи"
        )

        val singulariaTantum = setOf(
            "молоко", "золото", "серебро", "счастье", "любовь", "нефть", "молодёжь", "листва", "мебель"
        )

        // Проверка на исключения (множественное число)
        if (pluraliaTantum.contains(word)) {
            return Pair(Gender.MALE, Numeration.PLURAL)
        }

        // Проверка на исключения (единственное число)
        if (singulariaTantum.contains(word)) {
            return Pair(determineGender(word), Numeration.SINGLE)
        }

        // Определение числа по окончанию
        val number = determineNumber(word)

        // Определение рода (с учетом числа)
        val gender = when {
            masculineExceptions.contains(word) -> Gender.MALE
            feminineExceptions.contains(word) -> Gender.FEMALE
            neuterExceptions.contains(word) -> Gender.MEDIUM
            number == Numeration.PLURAL -> determineGenderForPlural(word)
            else -> determineGenderForSingular(word)
        }

        return Pair(gender, number)
    }

    private fun determineNumber(word: String): Numeration {
        return when {
            word.endsWith("ы") || word.endsWith("и") -> Numeration.PLURAL
            word.endsWith("а") || word.endsWith("я") -> {
                if (word.endsWith("ня") || word.endsWith("та") || word.endsWith("ья")) Numeration.PLURAL
                else Numeration.SINGLE
            }

            word.endsWith("о") || word.endsWith("е") || word.endsWith("э") -> Numeration.SINGLE
            else -> Numeration.SINGLE
        }
    }

    private fun determineGenderForSingular(word: String): Gender {
        return when {
            word.endsWith("ж") || word.endsWith("ш") || word.endsWith("ч") || word.endsWith("щ") -> Gender.MALE
            word.endsWith("й") || word.endsWith("ь") -> {
                if (word.endsWith("чь") || word.endsWith("шь") || word.endsWith("щь") || word.endsWith("жь")) Gender.MALE
                else Gender.FEMALE
            }

            word.endsWith("а") || word.endsWith("я") -> Gender.FEMALE
            word.endsWith("о") || word.endsWith("е") || word.endsWith("э") -> Gender.MEDIUM
            word.endsWith("мя") -> Gender.MEDIUM
            else -> Gender.MALE
        }
    }

    private fun determineGenderForPlural(word: String): Gender {
        // Для множественного числа род определяется по форме единственного числа
        val singularForm = when {
            word.endsWith("ы") -> word.dropLast(1) + "а"
            word.endsWith("и") -> {
                when {
                    word.endsWith("нии") -> word.dropLast(3) + "ие"
                    word.endsWith("ки") -> word.dropLast(2) + "ка"
                    else -> word.dropLast(1) + "ь"
                }
            }

            word.endsWith("а") -> word.dropLast(1) + "о"
            word.endsWith("я") -> word.dropLast(1) + "е"
            else -> word
        }

        return determineGenderForSingular(singularForm)
    }

    private fun determineGender(word: String): Gender {
        return determineGenderAndNumber(word).first
    }
}