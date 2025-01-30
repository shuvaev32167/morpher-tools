package ru.shuvaev.morpher.tools.type

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import ru.morpher.ws3.ClientBuilder
import ru.shuvaev.morpher.tools.cache.SqlLiteCache
import ru.shuvaev.morpher.tools.enams.Case

class PetrovichTest {
    @Test
    @Disabled
    fun morphNoun() {
        Assertions.assertEquals("женщине", Petrovich.morphNoun("женщина"))
        Assertions.assertEquals("мужчине", Petrovich.morphNoun("мужчина"))
        Assertions.assertEquals("гемофродиту", Petrovich.morphNoun("гемофродит"))
        Assertions.assertEquals("мальчику", Petrovich.morphNoun("мальчик"))
        Assertions.assertEquals("девочке", Petrovich.morphNoun("девочка"))
        Assertions.assertEquals("трапу", Petrovich.morphNoun("трап"))
        Assertions.assertEquals("томбою", Petrovich.morphNoun("томбой"))
        Assertions.assertEquals("девочке", Petrovich.morphNoun("девочке", case = Case.NOMINATIVUS))
    }

    @Test
    @Disabled
    fun morpherClient() {
        val client = ClientBuilder().build()
        val russian = client.russian()
        SqlLiteCache.getMorphedNoun("гипер")
        var result = russian.declension("гипер")
        println(result)
    }

}