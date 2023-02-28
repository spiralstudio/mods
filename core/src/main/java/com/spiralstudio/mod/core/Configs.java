package com.spiralstudio.mod.core;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

/**
 * @author Leego Yih
 */
public final class Configs {
    private static final String DIR = System.getProperty("user.dir");

    public static <T> T read(String name, Class<T> clazz) throws IOException {
        File file = getConfigFile(name);
        if (file == null) {
            return null;
        }
        try (InputStream is = Files.newInputStream(file.toPath())) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(is, clazz);
        }
    }

    public static <K, V> Map<K, V> readAsMap(String name) throws IOException {
        File file = getConfigFile(name);
        if (file == null) {
            return null;
        }
        try (InputStream is = Files.newInputStream(file.toPath())) {
            Yaml yaml = new Yaml();
            return yaml.load(is);
        }
    }

    public static File getConfigFile(String name) {
        File file = new File(DIR + "/code-mods/" + name + ".yml");
        if (!file.exists()) {
            file = new File(DIR + "/" + name + ".yml");
        }
        return file.exists() ? file : null;
    }
}
