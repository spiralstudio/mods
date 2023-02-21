package com.spiralstudio.mod.camera;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorBuilder;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.client.et ProjectXSceneView
 * @see com.threerings.tudey.a.t TudeySceneView
 * @see com.threerings.opengl.a.b CameraHandler
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
        addOffsetImplClass();
        redefineCameraHandler();
        addCameraCommands();
    }

    static void addOffsetImplClass() throws Exception {
        ClassBuilder.makeClass("com.spiralstudio.mod.camera.OffsetImpl")
                .interfaceClassName("com.threerings.opengl.a.b$a")
                .addFields(Arrays.stream(new String[]{"_tx", "_ty", "_tz", "_rx", "_ry", "_rz"})
                        .map(fieldName -> new FieldBuilder()
                                .fieldName(fieldName)
                                .typeName("float")
                                .modifiers(java.lang.reflect.Modifier.PUBLIC))
                        .collect(Collectors.toList()))
                .addConstructor(new ConstructorBuilder()
                        .parameters(new String[]{"float", "float", "float", "float", "float", "float"})
                        .body("{this._tx=$1;this._ty=$2;this._tz=$3;this._rx=$4;this._ry=$5;this._rz=$6;}")
                        .modifiers(Modifier.PUBLIC))
                .addMethod(new MethodBuilder()
                        .body("public boolean apply(com.threerings.math.Transform3D o) {\n" +
                                "    o.mL().h(this._tx, this._ty, this._tz);\n" +
                                "    com.threerings.math.Vector3f angles = o.mM().mv().h(this._rx, this._ry, this._rz);\n" +
                                "    o.mM().e(angles.x,angles.y,angles.z);\n" +
                                "    return true;\n" +
                                "}"))
                .build();
    }

    static void redefineCameraHandler() throws Exception {
        ClassBuilder.fromClass("com.threerings.opengl.a.b")
                .addField(new FieldBuilder()
                        .fieldName("_photomode")
                        .typeName("boolean")
                        .modifiers(java.lang.reflect.Modifier.PUBLIC))
                .addField(new FieldBuilder()
                        .fieldName("_oldt")
                        .typeName("com.threerings.math.Vector3f")
                        .modifiers(java.lang.reflect.Modifier.PUBLIC))
                .addField(new FieldBuilder()
                        .fieldName("_oldr")
                        .typeName("com.threerings.math.Vector3f")
                        .modifiers(java.lang.reflect.Modifier.PUBLIC))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public void enablePhotoMode() {\n" +
                                "    if (this._photomode) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    this._photomode = true;\n" +
                                "    this._oldt = new com.threerings.math.Vector3f();\n" +
                                "    this._oldr = new com.threerings.math.Vector3f();\n" +
                                "}"))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public void disablePhotoMode() {\n" +
                                "    if (!this._photomode) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    float tx = -this._oldt.x;\n" +
                                "    float ty = -this._oldt.y;\n" +
                                "    float tz = -this._oldt.z;\n" +
                                "    float rx = -this._oldr.x;\n" +
                                "    float ry = -this._oldr.y;\n" +
                                "    float rz = -this._oldr.z;\n" +
                                "    this._photomode = false;\n" +
                                "    this._oldt = null;\n" +
                                "    this._oldr = null;\n" +
                                "    if (tx == 0 && ty == 0 && tz == 0 && rx == 0 && ry == 0 && rz == 0) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)\n" +
                                "            java.lang.Class.forName(\"com.spiralstudio.mod.camera.OffsetImpl\")\n" +
                                "                    .getDeclaredConstructor(new Class[]{\n" +
                                "                            float.class, float.class, float.class,\n" +
                                "                            float.class, float.class, float.class})\n" +
                                "                    .newInstance(new java.lang.Object[]{\n" +
                                "                            java.lang.Float.valueOf((float) tx),\n" +
                                "                            java.lang.Float.valueOf((float) ty),\n" +
                                "                            java.lang.Float.valueOf((float) tz),\n" +
                                "                            java.lang.Float.valueOf((float) rx),\n" +
                                "                            java.lang.Float.valueOf((float) ry),\n" +
                                "                            java.lang.Float.valueOf((float) rz)\n" +
                                "                    });\n" +
                                "    this.addOffset(offset);\n" +
                                "}"))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public void tickPhotoMode() {\n" +
                                "    if (!this._photomode) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    float tx = 0;\n" +
                                "    float ty = 0;\n" +
                                "    float tz = 0;\n" +
                                "    float rx = 0;\n" +
                                "    float ry = 0;\n" +
                                "    float rz = 0;\n" +
                                "    int mx = org.lwjgl.input.Mouse.getDX();\n" +
                                "    int my = org.lwjgl.input.Mouse.getDY();\n" +
                                "    int mw = org.lwjgl.input.Mouse.getDWheel();\n" +
                                "    boolean lb = org.lwjgl.input.Mouse.isButtonDown(0);\n" +
                                "    boolean rb = org.lwjgl.input.Mouse.isButtonDown(1);\n" +
                                "    tz = mw * 0.02F;\n" +
                                "    if (lb) {\n" +
                                "        tx = -mx * 0.05F;\n" +
                                "        ty = -my * 0.05F;\n" +
                                "    } else if (rb) {\n" +
                                "        rz = mx * 0.005F;\n" +
                                "        rx = -my * 0.005F;\n" +
                                "    }\n" +
                                "    if (tx == 0 && ty == 0 && tz == 0 && rx == 0 && ry == 0 && rz == 0) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    System.out.println(\"[CameraKeyListener] Left=\" + lb + \", Right=\" + rb + \", mx=\" + Integer.toString(mx) + \", my=\" + Integer.toString(my) + \", mw=\" + Integer.toString(mw));\n" +
                                "    _oldt.x += tx;\n" +
                                "    _oldt.y += ty;\n" +
                                "    _oldt.z += tz;\n" +
                                "    _oldr.x += rx;\n" +
                                "    _oldr.y += ry;\n" +
                                "    _oldr.z += rz;\n" +
                                "    com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)\n" +
                                "            java.lang.Class.forName(\"com.spiralstudio.mod.camera.OffsetImpl\")\n" +
                                "                    .getDeclaredConstructor(new Class[]{\n" +
                                "                            float.class, float.class, float.class,\n" +
                                "                            float.class, float.class, float.class})\n" +
                                "                    .newInstance(new java.lang.Object[]{\n" +
                                "                            java.lang.Float.valueOf((float) tx),\n" +
                                "                            java.lang.Float.valueOf((float) ty),\n" +
                                "                            java.lang.Float.valueOf((float) tz),\n" +
                                "                            java.lang.Float.valueOf((float) rx),\n" +
                                "                            java.lang.Float.valueOf((float) ry),\n" +
                                "                            java.lang.Float.valueOf((float) rz)\n" +
                                "                    });\n" +
                                "    this.addOffset(offset);\n" +
                                "}\n"))
                .modifyMethod(new MethodModifier()
                        .methodName("updatePosition")
                        .insertAfter("this.tickPhotoMode();\n"))
                .build();
    }

/*public void enablePhotoMode() {
    if (this._photomode) {
        return;
    }
    this._photomode = true;
    this._oldt = new com.threerings.math.Vector3f();
    this._oldr = new com.threerings.math.Vector3f();
}*/

/*public void disablePhotoMode() {
    if (!this._photomode) {
        return;
    }
    float tx = -this._oldt.x;
    float ty = -this._oldt.y;
    float tz = -this._oldt.z;
    float rx = -this._oldr.x;
    float ry = -this._oldr.y;
    float rz = -this._oldr.z;
    this._photomode = false;
    this._oldt = null;
    this._oldr = null;
    if (tx == 0 && ty == 0 && tz == 0 && rx == 0 && ry == 0 && rz == 0) {
        return;
    }
    com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)
            java.lang.Class.forName("com.spiralstudio.mod.camera.OffsetImpl")
                    .getDeclaredConstructor(new Class[]{
                            float.class, float.class, float.class,
                            float.class, float.class, float.class})
                    .newInstance(new java.lang.Object[]{
                            java.lang.Float.valueOf((float) tx),
                            java.lang.Float.valueOf((float) ty),
                            java.lang.Float.valueOf((float) tz),
                            java.lang.Float.valueOf((float) rx),
                            java.lang.Float.valueOf((float) ry),
                            java.lang.Float.valueOf((float) rz)
                    });
    this.addOffset(offset);
}*/

/*public void tickPhotoMode() {
    if (!this._photomode) {
        return;
    }
    float tx = 0;
    float ty = 0;
    float tz = 0;
    float rx = 0;
    float ry = 0;
    float rz = 0;
    int mx = org.lwjgl.input.Mouse.getDX();
    int my = org.lwjgl.input.Mouse.getDY();
    int mw = org.lwjgl.input.Mouse.getDWheel();
    boolean lb = org.lwjgl.input.Mouse.isButtonDown(0);
    boolean rb = org.lwjgl.input.Mouse.isButtonDown(1);
    tz = mw * 0.02F;
    if (lb) {
        tx = -mx * 0.05F;
        ty = -my * 0.05F;
    } else if (rb) {
        rz = mx * 0.005F;
        rx = -my * 0.005F;
    }
    if (tx == 0 && ty == 0 && tz == 0 && rx == 0 && ry == 0 && rz == 0) {
        return;
    }
    System.out.println("[CameraKeyListener] Left=" + lb + ", Right=" + rb + ", mx=" + Integer.toString(mx) + ", my=" + Integer.toString(my) + ", mw=" + Integer.toString(mw));
    _oldt.x += tx;
    _oldt.y += ty;
    _oldt.z += tz;
    _oldr.x += rx;
    _oldr.y += ry;
    _oldr.z += rz;
    com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)
            java.lang.Class.forName("com.spiralstudio.mod.camera.OffsetImpl")
                    .getDeclaredConstructor(new Class[]{
                            float.class, float.class, float.class,
                            float.class, float.class, float.class})
                    .newInstance(new java.lang.Object[]{
                            java.lang.Float.valueOf((float) tx),
                            java.lang.Float.valueOf((float) ty),
                            java.lang.Float.valueOf((float) tz),
                            java.lang.Float.valueOf((float) rx),
                            java.lang.Float.valueOf((float) ry),
                            java.lang.Float.valueOf((float) rz)
                    });
    this.addOffset(offset);
}*/

    static void addCameraCommands() {
        // Add a field for caching KeyListener
        Commands.addField("_cameraKeyListener", "com.threerings.opengl.gui.event.e");
        Commands.addField("_cameraHudHidden", "boolean");
        // Add a command "/camon"
        Commands.addCommand("camon", "" +
                "System.out.println(\"Photo Mode On\");\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "com.threerings.opengl.a.b.class.getDeclaredMethod(\"enablePhotoMode\", new Class[0]).invoke(hud__.vk().Og(), new Object[0]);\n");
        // Add a command "/camoff"
        Commands.addCommand("camoff", "" +
                "System.out.println(\"Photo Mode Off\");\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "com.threerings.opengl.a.b.class.getDeclaredMethod(\"disablePhotoMode\", new Class[0]).invoke(hud__.vk().Og(), new Object[0]);\n");
        // Add a command "/hudon"
        Commands.addCommand("hudon", "" +
                "if (this._cameraHudHidden == true) {\n" +
                "    this._cameraHudHidden = false;\n" +
                "    com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "    com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "    hud__.uG();\n" +
                "}\n");
        // Add a command "/hudoff"
        Commands.addCommand("hudoff", "" +
                "if (this._cameraHudHidden == false) {\n" +
                "    this._cameraHudHidden = true;\n" +
                "    com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "    com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "    hud__.uH();\n" +
                "}\n");
    }

    public static void main(String[] args) {
    }
}
