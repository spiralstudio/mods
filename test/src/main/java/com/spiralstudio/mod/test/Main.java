package com.spiralstudio.mod.test;

import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorModifier;
import com.spiralstudio.mod.core.util.MethodModifier;

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
        testMonsterHudBar();
        testAdminCommand();
    }

    static void testMonsterHudBar() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.dungeon.client.g")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .insertAfter("this.aov.setVisible(true);\n"))
                .build();
    }

    static void testAdminCommand() throws Exception {
        // Does anyone have a token greater than zero?
        ClassBuilder.fromClass("com.threerings.crowd.data.TokenRing")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames(new String[]{"int"})
                        .insertBefore("System.out.println(\"token :\" + $1);"))
                .modifyMethod(new MethodModifier()
                        .methodName("aw")
                        .body("{return true;}"))
                .modifyMethod(new MethodModifier()
                        .methodName("ax")
                        .body("{return true;}"))
                .modifyMethod(new MethodModifier()
                        .methodName("iD")
                        .body("{return true;}"))
                .build();
    }

    public static void main(String[] args) {
    }
}
