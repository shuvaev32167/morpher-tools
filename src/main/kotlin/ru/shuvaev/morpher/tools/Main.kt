package ru.shuvaev.morpher.tools

import ru.shuvaev.morpher.tools.cache.SqlLiteCache
import ru.shuvaev.morpher.tools.cache.data.MorphologyDto

fun main() {
    val client = ru.morpher.ws3.ClientBuilder().build()
    var result = client.russian().declension("гипер")
    MorphologyDto.fromWs3Morpher(result)?.let {
        SqlLiteCache.saveMorphedNoun(it)
        println(it)
    }

    MorphologyDto.fromWs3Morpher(result)?.let {
        SqlLiteCache.saveMorphedNoun(it)
        println(it)
    }
}