package ru.nsu.ermakov.dsl;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.ermakov.entity.Config;

import java.io.File;
import java.io.IOException;

public class ConfigLoader {
    public Config loadConfig(String filePath) throws IOException {
        var config = new CompilerConfiguration();
        config.setScriptBaseClass("groovy.util.DelegatingScript");

        var shell = new GroovyShell(this.getClass().getClassLoader(), new Binding(), config);
        var script = (groovy.util.DelegatingScript) shell.parse(new File(filePath));

        var builder = new ConfigBuilder();
        script.setDelegate(builder);
        script.run();
        return builder.build();
    }
}
