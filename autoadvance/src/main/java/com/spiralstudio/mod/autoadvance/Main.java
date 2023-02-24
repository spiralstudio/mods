package com.spiralstudio.mod.autoadvance;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorModifier;
import com.spiralstudio.mod.core.util.FieldBuilder;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.Map;

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

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineElevatorChoiceWindow();
        addAutoAdvCommands();
        enableAutoAdv();
    }

    /**
     * Adds static field <code>_variableElapsed</code> to control the button.
     */
    static void redefineElevatorChoiceWindow() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.dungeon.client.Q")
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
                .build();
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

    static boolean enableAutoAdv() throws Exception {
        String dir = System.getProperty("user.dir");
        File file = new File(dir + "/code-mods/autoadvance.yml");
        if (!file.exists()) {
            file = new File(dir + "/autoadvance.yml");
        }
        if (!file.exists()) {
            return false;
        }
        try (InputStream is = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Config config = yaml.loadAs(is, Config.class);
            Map<String, String> autoadv = config.getAutoadv();
            if (autoadv != null && autoadv.getOrDefault("enabled", "false").equalsIgnoreCase("true")) {
                System.out.println("Auto Advance On (Configured)");
                return true;
            }
        }
        return false;
    }

    public static class Config {
        private Map<String, String> autoadv;

        public Map<String, String> getAutoadv() {
            return autoadv;
        }

        public void setAutoadv(Map<String, String> autoadv) {
            this.autoadv = autoadv;
        }
    }

    public static void main(String[] args) {
    }
}
