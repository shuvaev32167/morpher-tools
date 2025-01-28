package ru.shuvaev.morpher.tools.cache

import ru.shuvaev.morpher.tools.cache.data.MorphologyDto

interface Cache {
    fun getMorphed(word: String): MorphologyDto?
    fun saveMorphed(morphologyDto: MorphologyDto): MorphologyDto
}