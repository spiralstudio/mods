package com.spiralstudio.mod.autoadvance;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorModifier;
import com.spiralstudio.mod.core.util.FieldBuilder;

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

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineElevatorChoiceWindow();
        addAutoAdvCommands();
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
                                "    this._elapsed = 0F;\n" +
                                "} else {\n" +
                                "    this._elapsed = ((java.lang.Float) _ve).floatValue();\n" +
                                "}\n"))
                .build();
    }

    static void addAutoAdvCommands() {
        // Add a command "/autoadvon"
        Commands.addCommand("autoadvon", "" +
                "System.out.println(\"Auto Advance On\");\n" +
                "java.lang.Class.forName(\"com.threerings.projectx.dungeon.client.Q\").getDeclaredField(\"_variableElapsed\").set(null,Float.valueOf(100F));\n");
        // Add a command "/autoadvoff"
        Commands.addCommand("autoadvoff", "" +
                "System.out.println(\"Auto Advance Off\");\n" +
                "java.lang.Class.forName(\"com.threerings.projectx.dungeon.client.Q\").getDeclaredField(\"_variableElapsed\").set(null,Float.valueOf(0F));\n");
    }

    public static void main(String[] args) {
    }
}
