package com.threerings.mod.config;

import com.threerings.config.ManagedConfig;

/**
 * @author Leego Yih
 */
public class ModConfig extends ManagedConfig {

    static {
        init();
    }

    static void init() {
        String classes = System.getProperties().getProperty("mod.classes");
        if (classes == null || classes.length() == 0) {
            System.out.println("No mods");
            return;
        }
        for (String className : classes.split(",")) {
            className = className.trim();
            if (className.length() == 0) {
                continue;
            }
            try {
                Class.forName(className);
            } catch (Exception e) {
                System.out.println("Load class '" + className + "' failed\n");
                e.printStackTrace();
            }
        }
    }
}
