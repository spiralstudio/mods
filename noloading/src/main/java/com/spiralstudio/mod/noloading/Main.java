package com.spiralstudio.mod.noloading;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * @author Leego Yih
 * @see com.threerings.tudey.a.k TudeySceneView
 * @see com.threerings.projectx.client.bi LoadingWindow
 */
public class Main {
    private static boolean mounted = false;

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineLoadingWindow();
        //redefineProjectXSceneView();
        redefineScriptRunner();
    }

    /**
     * Hides the loading screen and removes the fading animations.
     */
    static void redefineLoadingWindow() {
        ClassPool.from("com.threerings.projectx.client.bi")
                .modifyMethod(new MethodModifier()
                        .methodName("J") // update
                        .paramTypeNames("float")
                        .insertBefore("" +
                                "if ($1 <= 0.0F) {\n" +
                                "    this.setAlpha(0.0F);\n" +
                                "    this.getComponent(\"depth\").setAlpha(1.0F);\n" +
                                "    this.getComponent(\"progress\").setAlpha(1.0F);\n" +
                                "    this.getComponent(\"gate\").setAlpha(1.0F);\n" +
                                "    this.getComponent(\"zonename\").setAlpha(1.0F);\n" +
                                "}" +
                                ""))
                .modifyMethod(new MethodModifier()
                        .methodName("vK") // fadeOut
                        .body("" +
                                "{\n" +
                                "    ((com.threerings.opengl.gui.Label) this.getComponent(\"progress\")).setText(\"\");\n" +
                                "    this._ctx.xb().getStreamGain().value = 0.0F;\n" +
                                "    if (this.afM) {\n" +
                                "        this.acf.yO().vd();\n" +
                                "        this._ctx.xa().moveToTop(this);\n" +
                                "        this._ctx.xg().KQ();\n" +
                                "    }\n" +
                                "    this.acf.yO().uG();\n" +
                                "    this._ctx.xa().addTickParticipant(new com.threerings.projectx.client.bj(this,\n" +
                                "            new Object[]{\"fadeBackground\", Float.valueOf(0.0F), \"linger\", Float.valueOf(0.0F), \"fadeTitle\", Float.valueOf(0.0F)}));\n" +
                                "}"));
    }

    static void redefineProjectXSceneView() {
        ClassPool.from("com.threerings.projectx.client.et")
                .modifyMethod(new MethodModifier()
                        .methodName("j")
                        .paramTypeNames("java.lang.Runnable")
                        .insertBefore("" +
                                "{\n" +
                                "    if (this.alY.isAdded()) {\n" +
                                "        this.alY.saveState();\n" +
                                "        this.alY.uH();\n" +
                                "    }\n" +
                                "    com.threerings.opengl.gui.aj var2;\n" +
                                "    (var2 = new com.threerings.opengl.gui.aj(this._ctx, (com.threerings.opengl.gui.d.g) null)).setLayer(1);\n" +
                                "    this._ctx.xa().addWindow(var2);\n" +
                                "    com.threerings.opengl.gui.a.d var3 = new com.threerings.opengl.gui.a.d(com.threerings.opengl.renderer.Color4f.BLACK);\n" +
                                "    var2.setBackground(0, var3);\n" +
                                "    var2.setBackground(1, var3);\n" +
                                "    this._ctx.xa().addTickParticipant(new com.threerings.projectx.client.eu(this, new Object[]{\"fade\", Float.valueOf(0.0F)}, var2, $1));\n" +
                                "}"));
    }

    static void redefineScriptRunner() {
        ClassPool.from("com.threerings.opengl.gui.ay$a")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .paramTypeNames("float")
                        .insertBefore("$1 += 1000.0F;"));
    }

    public static void main(String[] args) {
    }
}
