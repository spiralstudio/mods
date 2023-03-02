package com.spiralstudio.mod.test;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Registers;
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

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        //testMonsterHudBar();
        //testAdminCommand();
        testMap();
        testBaseItemListPanel();
    }

    static void testMonsterHudBar() {
        ClassPool.from("com.threerings.projectx.dungeon.client.g")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .insertAfter("this.aov.setVisible(true);\n"));
    }

    static void testAdminCommand() {
        // Does anyone have a token greater than zero?
        ClassPool.from("com.threerings.crowd.data.TokenRing")
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
                        .body("{return true;}"));
    }

    static void testMap() {
        // Show monsters on map
        ClassPool.from("com.threerings.projectx.map.client.a")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("com.threerings.tudey.util.m", "com.threerings.tudey.a.k")
                        .insertAfter("" +
                                "this.aMi.dY(ProjectXCodes.avh | MaskEditor.n(\"collision\", \"player\") | MaskEditor.n(\"collision\", \"player_barrier\") | MaskEditor.n(\"collision\", \"monster\"));\n"));
        // Zoom map
        ClassPool.from("com.threerings.projectx.map.client.c")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .insertAfter("" +
                                "if (this.aMq != null) {\n" +
                                "    com.threerings.opengl.gui.e.c var3 = this.aMq.getPreferredSize(-1, -1);\n" +
                                "    var3.height = this.aqE.getRoot().getDisplayHeight() * 2;\n" +
                                "    var3.width = this.aqE.getRoot().getDisplayWidth() * 2;\n" +
                                "    this.aMq.setPreferredSize(var3);\n" +
                                "}"));
    }

    static void testBaseItemListPanel() {
        ClassPool.from("com.threerings.projectx.item.client.j")
                .modifyMethod(new MethodModifier()
                        .methodName("d")
                        .paramTypeNames("com.threerings.projectx.item.client.j$b")
                        .body("{" +
                                "com.threerings.opengl.gui.ay _pill = a(this._ctx, $1.getName(), $1.Bo(), $1.getReference(), $1.Bf(), this.Bn(), this.HR(), false);" +
                                "System.out.println(_pill);" +
                                "return _pill;" +
                                "}"));
    }

    public String printStackTrace() {
        java.lang.Throwable source = new java.lang.Throwable();
        java.lang.StackTraceElement[] es = source.getStackTrace();
        StringBuilder sb = new StringBuilder();
        if (null != es) {
            for (StackTraceElement e : es) {
                sb.append(e.getClassName());
                sb.append(".").append(e.getMethodName());
                sb.append("(").append(e.getFileName()).append(":");
                sb.append(e.getLineNumber()).append(")").append("\n");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    }
}
