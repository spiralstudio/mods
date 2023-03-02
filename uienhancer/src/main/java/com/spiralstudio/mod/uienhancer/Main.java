package com.spiralstudio.mod.uienhancer;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.MethodBuilder;

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
        redefineHudWindow();
        addCommands();
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
