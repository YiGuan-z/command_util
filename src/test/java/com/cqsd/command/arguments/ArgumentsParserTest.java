package com.cqsd.command.arguments;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArgumentsParserTest {
    String[] arguments= {"-p","8080","-path","/home","-http","3"};

    @Test
    void getArguments() {
        final var config = new ArgumentsParser(arguments);
        Assertions.assertEquals("8080",config.getArguments("p").unwarp());
        Assertions.assertEquals("/home",config.getArguments("path").unwarp());
        Assertions.assertEquals("3",config.getArguments("http").unwarp());
    }
}