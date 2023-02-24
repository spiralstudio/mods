package com.spiralstudio.mod.noloading;

import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * @author Leego Yih
 * @see com.threerings.tudey.a.k TudeySceneView
 * @see com.threerings.projectx.client.bi LoadingWindow
 */
public class Main {
    private static boolean mounted = false;

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineLoadingWindow();
    }

    /**
     * Hides the loading screen and removes the fading animations.
     */
    static void redefineLoadingWindow() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.client.bi")
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
                                "}"))
                .build();
    }

    public static void main(String[] args) {
    }
}
