package ru.nsu.ermakov.util;

import java.util.Objects;

/**
 * Простая утилита для форматированного логирования в stdout.
 */
public final class Log {
    private Log() {}

    public static void info(String format, Object... args) {
        String msg = format == null ? "" : (args == null || args.length == 0 ? format : String.format(format, args));
        System.out.println(msg);
    }

    public static void info(Object obj) {
        System.out.println(Objects.toString(obj, ""));
    }

    public static void error(String format, Object... args) {
	    info(format, args);
    }
}
