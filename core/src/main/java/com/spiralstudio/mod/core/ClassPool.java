package com.spiralstudio.mod.core;

import com.spiralstudio.mod.core.util.ClassBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Leego Yih
 */
public class ClassPool {
    private static final Map<String, ClassBuilder> pool = new LinkedHashMap<>();

    public static ClassBuilder make(String className) {
        return pool.computeIfAbsent(className, ClassBuilder::makeClass);
    }

    public static ClassBuilder from(String className) {
        return pool.computeIfAbsent(className, ClassBuilder::fromClass);
    }

    protected static void init() throws Exception {
        for (Map.Entry<String, ClassBuilder> entry : pool.entrySet()) {
            entry.getValue().build();
        }
    }
}
