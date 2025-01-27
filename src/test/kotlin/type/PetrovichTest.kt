package type

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.shuvaev.morpher.tools.enams.Case
import ru.shuvaev.morpher.tools.type.Petrovich

class PetrovichTest {
    @Test
    fun morphNoun() {
        assertEquals("женщине", Petrovich.morphNoun("женщина"))
        assertEquals("мужчине", Petrovich.morphNoun("мужчина"))
        assertEquals("гемофродиту", Petrovich.morphNoun("гемофродит"))
        assertEquals("мальчику", Petrovich.morphNoun("мальчик"))
        assertEquals("девочке", Petrovich.morphNoun("девочка"))
        assertEquals("", Petrovich.morphNoun("девочка", case = Case.NOMINATIVUS))
    }

}