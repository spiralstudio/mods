package com.spiralstudio.mod.autoadvance;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Configs;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ConstructorModifier;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;

/**
 * Enter "/autoadvon" to enable.
 * Enter "/autoadvoff" to disable.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.dungeon.client.Q ElevatorChoiceWindow
 */
public class Main {
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineElevatorChoiceWindow();
        redefineEndOfLevelWindow();
        addAutoAdvCommands();
        enableAutoAdv();
    }

    /**
     * Adds static field <code>_variableElapsed</code> to control the button.
     */
    static void redefineElevatorChoiceWindow() {
        ClassPool.from("com.threerings.projectx.dungeon.client.Q")
                .addField(new FieldBuilder()
                        .fieldName("_variableElapsed")
                        .typeName("java.lang.Float")
                        .modifiers(Modifier.PUBLIC | Modifier.STATIC))
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("com.threerings.projectx.util.A", "com.threerings.projectx.client.et", "com.threerings.projectx.dungeon.client.EndOfLevelWindow")
                        .insertAfter("" +
                                "java.lang.Object _ve = com.threerings.projectx.dungeon.client.Q.class.getDeclaredField(\"_variableElapsed\").get(null);\n" +
                                "if (_ve == null) {\n" +
                                (enableAutoAdv() ? "this._elapsed = 100F;\n" : "this._elapsed = 0F;\n") +
                                "} else {\n" +
                                "    this._elapsed = ((java.lang.Float) _ve).floatValue();\n" +
                                "}\n"))
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .paramTypeNames("float")
                        .insertBefore("$1 += 1000.0F;"));
    }

    static void redefineEndOfLevelWindow() {
        ClassPool.from("com.threerings.projectx.dungeon.client.EndOfLevelWindow")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .paramTypeNames("float")
                        .insertBefore("$1 += 1000.0F;"));
    }

    static void addAutoAdvCommands() {
        // Add a command "/autoadvon"
        Commands.addCommand("autoadvon", "" +
                "System.out.println(\"Auto Advance On\");\n" +
                "java.lang.Class.forName(\"com.threerings.projectx.dungeon.client.Q\").getDeclaredField(\"_variableElapsed\").set(null, Float.valueOf(100F));\n");
        // Add a command "/autoadvoff"
        Commands.addCommand("autoadvoff", "" +
                "System.out.println(\"Auto Advance Off\");\n" +
                "java.lang.Class.forName(\"com.threerings.projectx.dungeon.client.Q\").getDeclaredField(\"_variableElapsed\").set(null, Float.valueOf(0F));\n");
    }

    static boolean enableAutoAdv() {
        try {
            Config config = Configs.read("autoadvance", Config.class);
            return config != null && config.getAutoadv() != null && config.getAutoadv().isEnabled();
        } catch (Exception e) {
            System.out.println("Failed to read config: " + e.getMessage());
            return false;
        }
    }

    public static class Config {
        private Autoadv autoadv;

        public Autoadv getAutoadv() {
            return autoadv;
        }

        public void setAutoadv(Autoadv autoadv) {
            this.autoadv = autoadv;
        }

        public static class Autoadv {
            private boolean enabled;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    public static void main(String[] args) {
    }
}
