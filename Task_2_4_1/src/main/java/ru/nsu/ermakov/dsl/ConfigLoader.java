package ru.nsu.ermakov.dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.ermakov.entity.Config;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Загружает конфигурацию курса из Groovy DSL-файла, поддерживает include.
 */
public class ConfigLoader {

    /**
     * Загружает конфигурацию по пути к файлу.
     * @param filePath путь к DSL файлу
     * @return объект Config
     * @throws IOException при ошибках чтения или синтаксиса
     */
    public Config loadConfig(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IOException("Config path is empty");
        }

        Path configPath = Path.of(filePath).toAbsolutePath().normalize();
        return loadConfig(configPath, new HashSet<>());
    }

    /**
     * Внутренний загрузчик с поддержкой detection циклических include.
     */
    Config loadConfig(Path configPath, Set<Path> loadingStack) throws IOException {
        if (configPath == null) {
            throw new IOException("Config path is null");
        }
        if (!Files.exists(configPath)) {
            throw new IOException("Config file does not exist: " + configPath);
        }
        if (!loadingStack.add(configPath)) {
            throw new IOException("Cyclic include detected for config: " + configPath);
        }

        var config = new CompilerConfiguration();
        config.setScriptBaseClass("groovy.util.DelegatingScript");

        var shell = new GroovyShell(this.getClass().getClassLoader(), new Binding(), config);
        var script = (groovy.util.DelegatingScript) shell.parse(new File(configPath.toString()));

        Path baseDir = configPath.getParent();
        if (baseDir == null) {
            baseDir = Path.of(System.getProperty("user.dir"));
        }

        var builder = new ConfigBuilder(this, baseDir, loadingStack);
        script.setDelegate(builder);

        try {
            script.run();
            return builder.build();
        } finally {
            loadingStack.remove(configPath);
        }
    }
}
