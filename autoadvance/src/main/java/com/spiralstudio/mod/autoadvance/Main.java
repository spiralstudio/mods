package com.spiralstudio.mod.autoadvance;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorModifier;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;

/**
 * @author Leego Yih
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
        ClassBuilder.fromClass("com.threerings.projectx.dungeon.client.Q")
                .addField(new FieldBuilder()
                        .fieldName("DEFAULT_ELAPSED")
                        .typeName("java.lang.Float")
                        .modifiers(Modifier.PUBLIC | Modifier.STATIC))
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames(new String[]{"com.threerings.projectx.util.A", "com.threerings.projectx.client.et", "com.threerings.projectx.dungeon.client.EndOfLevelWindow"})
                        .insertAfter("" +
                                "java.lang.Object DE__ = com.threerings.projectx.dungeon.client.Q.class.getDeclaredField(\"DEFAULT_ELAPSED\").get(null);\n" +
                                "if (DE__ == null) {\n" +
                                "    DE__ = Float.valueOf(0F);\n" +
                                "}\n" +
                                "this._elapsed = ((java.lang.Float) DE__).floatValue();\n"))
                .modifyMethod(new MethodModifier()
                        .methodName("actionPerformed")
                        .insertAfter("System.out.println(\"Click button Advance\");"))
                .build();
        // Add a command "/autoadvon"
        Commands.addCommand("autoadvon", "" +
                "System.out.println(\"Auto Advance On\");\n" +
                "java.lang.Class.forName(\"com.threerings.projectx.dungeon.client.Q\").getDeclaredField(\"DEFAULT_ELAPSED\").set(null,Float.valueOf(100F));\n");
        // Add a command "/autoadvoff"
        Commands.addCommand("autoadvoff", "" +
                "System.out.println(\"Auto Advance Off\");\n" +
                "java.lang.Class.forName(\"com.threerings.projectx.dungeon.client.Q\").getDeclaredField(\"DEFAULT_ELAPSED\").set(null,Float.valueOf(0F));\n");
    }

    public static void main(String[] args) {
    }
}
