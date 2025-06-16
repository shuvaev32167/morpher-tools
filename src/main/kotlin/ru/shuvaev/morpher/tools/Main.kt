package ru.shuvaev.morpher.tools

import com.github.shuvaev32167.morpher.ws3.client.ClientBuilder
import ru.shuvaev.morpher.tools.cache.SqlLiteCache
import ru.shuvaev.morpher.tools.cache.data.MorphologyDto

fun main() {
    val client = ClientBuilder().build()
    SqlLiteCache.getMorphedNoun("гипер")
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