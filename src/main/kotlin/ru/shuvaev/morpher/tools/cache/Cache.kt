package ru.shuvaev.morpher.tools.cache

import ru.shuvaev.morpher.tools.cache.data.MorphGenderDto
import ru.shuvaev.morpher.tools.cache.data.MorphNameDto
import ru.shuvaev.morpher.tools.cache.data.MorphologyDto

interface Cache {
    fun getMorphedNoun(word: String): MorphologyDto?
    fun saveMorphedNoun(data: MorphologyDto): MorphologyDto

    fun getMorphedGender(word: String): MorphGenderDto?
    fun saveMorphedGender(data: MorphGenderDto): MorphGenderDto

    fun getMorphedFirstName(word: String): MorphNameDto?
    fun saveMorphedFirstName(data: MorphNameDto): MorphNameDto

    fun getMorphedLastName(word: String): MorphNameDto?
    fun saveMorphedLastName(data: MorphNameDto): MorphNameDto
}