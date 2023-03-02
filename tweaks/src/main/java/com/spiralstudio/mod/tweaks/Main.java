package com.spiralstudio.mod.tweaks;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * @author Leego Yih
 * @see com.threerings.tudey.data.TudeySceneConfig
 * @see com.threerings.tudey.a.a TudeySceneController
 * @see com.threerings.tudey.a.k TudeySceneView
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
        redefineTudeySceneView();
        redefineTudeySceneController();
        redefineTudeySceneConfig();
    }

    static void redefineTudeySceneView() {
        ClassPool.from("com.threerings.tudey.a.k")
                .modifyMethod(new MethodModifier()
                        .methodName("Ol") // getBufferDelay
                        .body("{ return 0; }"));
    }

    static void redefineTudeySceneController() {
        ClassPool.from("com.threerings.tudey.a.a")
                .modifyMethod(new MethodModifier()
                        .methodName("tick") // tick -> transmitInput
                        .paramTypeNames("float")
                        .body("{\n" +
                                "    ++this.aXL;\n" +
                                "    this.P($1);\n" +
                                "    long var2;\n" +
                                "    if ((var2 = com.samskivert.util.ak.currentTimeMillis()) - this.Dt >= (long) this.um() && this.aTY > 0) {\n" +
                                "        com.threerings.tudey.a.a var8 = this;\n" +
                                "        int var4;\n" +
                                "        int var5 = (var4 = this.aXs.Oo()) + Math.max(0, this.aXs.uA() - Math.round((float) this.aXs.Om() * 1.1F));\n" +
                                "        while (var8.aTZ.size() > 1 && var5 >= ((com.threerings.tudey.data.InputFrame) var8.aTZ.get(0)).Fy()) {\n" +
                                "            var8.aTZ.remove(0);\n" +
                                "        }\n" +
                                "        if (!var8._ctx$31b2d882.rq().pd()) {\n" +
                                "            var8.aTX.tudeySceneService.a(var8.aTY, var4, (com.threerings.tudey.data.InputFrame[]) var8.aTZ.toArray(new com.threerings.tudey.data.InputFrame[var8.aTZ.size()]));\n" +
                                "            var8.aTZ.clear();\n" +
                                "        } else {\n" +
                                "            var5 = 64;\n" +
                                "            int var6 = 0;\n" +
                                "            for (int var7 = var8.aTZ.size(); var6 < var7; ++var6) {\n" +
                                "                var5 += ((com.threerings.tudey.data.InputFrame) var8.aTZ.get(var6)).Ce();\n" +
                                "            }\n" +
                                "            for (var6 = 1048576 * var8.um() / 1000; var5 > var6; var5 -= ((com.threerings.tudey.data.InputFrame) var8.aTZ.remove(0)).Ce()) {\n" +
                                "            }\n" +
                                "            var8.aTX.tudeySceneService.b(var8.aTY, var4, (com.threerings.tudey.data.InputFrame[]) var8.aTZ.toArray(new com.threerings.tudey.data.InputFrame[var8.aTZ.size()]));\n" +
                                "        }\n" +
                                "        this.Dt = var2;\n" +
                                "    }\n" +
                                "}"));
    }

    static void redefineTudeySceneConfig() {
        ClassPool.from("com.threerings.tudey.data.TudeySceneConfig")
                .modifyMethod(new MethodModifier()
                        .methodName("ec") // getInputAdvance
                        .paramTypeNames("int")
                        .body("{ return this.um() + $1 + 5; }"))
                .modifyMethod(new MethodModifier()
                        .methodName("um") // getTransmitInterval
                        .body("{ return 70; }"));
    }

    public static void main(String[] args) {
    }
}
