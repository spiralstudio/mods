package com.spiralstudio.mod.uienhancer;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ConstructorModifier;
import com.spiralstudio.mod.core.util.MethodBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * TODO
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.aC HudWindow
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
        redefineMap();
        //redefineHudWindow();
        //addCommands();
    }

    static void redefineMap() {
        // Show monsters on map
        ClassPool.from("com.threerings.projectx.map.client.a")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("com.threerings.tudey.util.m", "com.threerings.tudey.a.k")
                        .insertAfter("" +
                                "this.aMi.dY(com.threerings.projectx.data.ProjectXCodes.avh | com.threerings.editor.swing.editors.MaskEditor.n(\"collision\", \"player\") | com.threerings.editor.swing.editors.MaskEditor.n(\"collision\", \"player_barrier\") | com.threerings.editor.swing.editors.MaskEditor.n(\"collision\", \"monster\"));\n"));
        // Zoom map
        ClassPool.from("com.threerings.projectx.map.client.c")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("com.threerings.tudey.util.m", "com.threerings.tudey.a.k")
                        .body("" +
                                "        {\n" +
                                "            super($1, true);\n" +
                                "            com.threerings.tudey.util.m var1 = $1;\n" +
                                "            com.threerings.tudey.a.k var2 = $2;\n" +
                                "            this.aqE = var1;\n" +
                                "            this._view = var2;\n" +
                                "            this.aMq = this.b(this.aqE, this._view);\n" +
                                "            if (this.aMq != null) {\n" +
                                "                this.add(this.aMq, new java.lang.Integer(4));\n" +
                                "            }\n" +
                                "            if (this.aMq != null) {\n" +
                                "                com.threerings.opengl.gui.e.c var3 = this.aMq.getPreferredSize(-1, -1);\n" +
                                "                var3.width = this.aqE.getRoot().getDisplayWidth() * 10;\n" +
                                "                var3.height = this.aqE.getRoot().getDisplayHeight() * 10;\n" +
                                "                this.aMq.setPreferredSize(var3);\n" +
                                "                this.aMq.setSize(var3.width, var3.height);\n" +
                                "                this.pack();\n" +
                                "            }\n" +
                                "            this.setVisible(true);\n" +
                                "            this.setHoverable(false);\n" +
                                "        }"))
                .modifyMethod(new MethodModifier()
                        .methodName("bQ")
                        .paramTypeNames("boolean")
                        .insertBefore("" +
                                "if (this.aMq != null) {\n" +
                                "    com.threerings.opengl.gui.e.c var3 = this.aMq.getPreferredSize(-1, -1);\n" +
                                "    System.out.println(\"Width:\" + Integer.toString(var3.width) + \", Height:\" + Integer.toString(var3.height));" +
                                "}\n" +
                                "System.out.println(\"Show map: \" + Boolean.toString($1));\n"));
    }

    static void redefineHudWindow() {
        ClassPool.from("com.threerings.projectx.client.aC")
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public void resize() {\n" +
                                "    float scale = 5.0F;\n" +
                                "    com.threerings.opengl.gui.q[] components = new com.threerings.opengl.gui.q[]{\n" +
                                "            this.aeW, this.afn, this.aeX, this.afs, this.aft, this.afu, this.afv, this.afw, this.afq, this.afx};\n" +
                                "    for (int i = 0; i < components.length; i++) {\n" +
                                "        com.threerings.opengl.gui.q component = components[i];\n" +
                                "        if (component != null) {\n" +
                                "            component.setSize((int) ((float) component.getWidth() * scale), (int) ((float) component.getHeight() * scale));\n" +
                                "            component.render(this._ctx.getRenderer());\n" +
                                "        }\n" +
                                "    }\n" +
                                "}"));
    }

    static void addCommands() {
        Commands.addCommand("resize", "" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "java.lang.reflect.Method resizeMethod = com.threerings.projectx.client.aC.class.getDeclaredMethod(\"resize\", new java.lang.Class[0]);\n" +
                "resizeMethod.setAccessible(true);\n" +
                "resizeMethod.invoke(hud__, new java.lang.Object[0]);\n");
    }

    public static void main(String[] args) {
    }
}
