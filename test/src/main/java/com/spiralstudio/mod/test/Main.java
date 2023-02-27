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
        //testMonsterHudBar();
        //testAdminCommand();
        testMap();
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

    static void testMap() throws Exception {
        // Show monsters on map
        ClassBuilder.fromClass("com.threerings.projectx.map.client.a")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("com.threerings.tudey.util.m", "com.threerings.tudey.a.k")
                        .insertAfter("" +
                                "this.aMi.dY(ProjectXCodes.avh | MaskEditor.n(\"collision\", \"player\") | MaskEditor.n(\"collision\", \"player_barrier\") | MaskEditor.n(\"collision\", \"monster\"));\n"))
                .build();
        // Zoom map
        ClassBuilder.fromClass("com.threerings.projectx.map.client.c")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .insertAfter("" +
                                "if (this.aMq != null) {\n" +
                                "    com.threerings.opengl.gui.e.c var3 = this.aMq.getPreferredSize(-1, -1);\n" +
                                "    var3.height = this.aqE.getRoot().getDisplayHeight() * 2;\n" +
                                "    var3.width = this.aqE.getRoot().getDisplayWidth() * 2;\n" +
                                "    this.aMq.setPreferredSize(var3);\n" +
                                "}"))
                .build();
    }

    public static void main(String[] args) {
    }
}
