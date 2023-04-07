package com.cqsd.command.arguments;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandLineOptionTest {
    Logger log = LogManager.getLogger(CommandLineOptionTest.class);
    String[] arguments = {"-p", "8080", "-path", "/home", "-http", "3","-h"};
    String[] arguments1 = {"-p", "8080", "-http", "3"};


    @Test
    void parseOption() {
        final var portOption = Option.newOption("p", "设置监听端口", "8080");
        final var pathOption = Option.newOption("path", "设置路径");
        final var httpOption = Option.newOption("http", "设置http协议版本", "3");
        final var hOption = Option.newOption("h", "设置http协议版本", "3");
        final var result = CommandLineOption
                .create()
                .addOption(portOption, pathOption, httpOption,hOption)
                .parse(arguments);
        if (result.isErr()) {
            final var msg = result.err();
            log.error(msg);
            return;
        }
        final var commandLineOption = result.unwarp();
        assertEquals(8080, commandLineOption.getArguments(portOption, int.class));
        assertEquals("/home", commandLineOption.getArguments(pathOption, String.class));
        assertEquals(3, commandLineOption.getArguments(httpOption, int.class));
        assertEquals(3, commandLineOption.getArguments(hOption, int.class));

    }

    @Test
    void testHelpMsg() {
        final var portOption = Option.newOption("p", "设置监听端口", "8080");
        final var pathOption = Option.newOption("path", "设置路径");
        final var httpOption = Option.newOption("http", "设置http协议版本", "3");
        final var result = CommandLineOption
                .create()
                .addOption(portOption,pathOption,httpOption)
                .parse(arguments1);
        if (result.isErr()) {
            final var msg = result.err();
            log.error(msg);
            return;
        }
        final var commandLineOption = result.unwarp();
        assertEquals(8080, commandLineOption.getArguments("p", int.class));
        assertEquals("/home", commandLineOption.getArguments("path", String.class));
        assertEquals(3, commandLineOption.getArguments("http", int.class));
    }
    @Test
    void testBox(){
        final var portOption = Option.newOption("p", "设置监听端口", "8080");
        final var pathOption = Option.newOption("path", "设置路径");
        final var httpOption = Option.newOption("http", "设置http协议版本", "3");
        final var pBox = portOption.box();
        final var result = CommandLineOption
                .create()
                .getArguments("p",portOption.box())
                .addOption(portOption, pathOption, httpOption)
                .parse(arguments);
        if (result.isErr()) {
            final var msg = result.err();
            log.error(msg);
            return;
        }
        final var commandLineOption = result.unwarp();
        assertEquals("8080",pBox.instance());
        assertEquals("8080",portOption.box().instance());
        assertEquals(8080, commandLineOption.getArguments("p", int.class));
        assertEquals("/home", commandLineOption.getArguments("path", String.class));
        assertEquals(3, commandLineOption.getArguments("http", int.class));
    }
}