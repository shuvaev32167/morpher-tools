module ru.shuvaev.morpher.tools {
    exports ru.shuvaev.morpher.tools;
    exports ru.shuvaev.morpher.tools.enams;
    exports ru.shuvaev.morpher.tools.type;
    exports ru.shuvaev.morpher.tools.cache;

    requires java.sql;
    requires petrovich4j;
    requires morpher.w3.client;
    requires shuvaev.morpher.ws_web.client;
}