package type

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import ru.shuvaev.morpher.tools.cache.SqlLiteCache
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.type.Petrovich

class PetrovichTest {
    @Test
    @Disabled
    fun morphNoun() {
        assertEquals("женщине", Petrovich.morphNoun("женщина"))
        assertEquals("мужчине", Petrovich.morphNoun("мужчина"))
        assertEquals("гемофродиту", Petrovich.morphNoun("гемофродит"))
        assertEquals("мальчику", Petrovich.morphNoun("мальчик"))
        assertEquals("девочке", Petrovich.morphNoun("девочка"))
        assertEquals("трапу", Petrovich.morphNoun("трап"))
        assertEquals("томбою", Petrovich.morphNoun("томбой"))
        assertEquals("девочке", Petrovich.morphNoun("девочке", case = Case.NOMINATIVUS))
    }

    @Test
    @Disabled
    fun morpherClient() {
        val client = ru.morpher.ws3.ClientBuilder().build()
        val russian = client.russian()
        SqlLiteCache.getMorphedNoun("гипер")
        var result = russian.declension("гипер")
        println(result)
    }

}