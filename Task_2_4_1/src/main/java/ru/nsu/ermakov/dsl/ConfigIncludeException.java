package ru.nsu.ermakov.dsl;

/**
 * Исключение для ошибок include() при загрузке конфигурации DSL.
 */
public class ConfigIncludeException extends RuntimeException {
    public ConfigIncludeException(String message) {
        super(message);
    }

    public ConfigIncludeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigIncludeException(Throwable cause) {
        super(cause);
    }
}

