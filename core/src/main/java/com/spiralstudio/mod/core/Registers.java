package com.spiralstudio.mod.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Leego Yih
 */
public class Registers {
    private static final List<Class<?>> classes = new ArrayList<>();

    /**
     * Registers a class to guarantee its loading order.
     *
     * @param clazz class to be registered.
     */
    public static void add(Class<?> clazz) {
        classes.add(clazz);
    }

    public static void init() throws Exception {
        // Guarantee all mods are installed first
        if (classes.isEmpty()) {
            return;
        }
        for (Class<?> clazz : classes) {
            try {
                Method method = clazz.getDeclaredMethod("mount");
                method.setAccessible(true);
                method.invoke(null);
            } catch (Exception ignored) {
            }
        }
    }
}
