package com.spiralstudio.mod.camera;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

import java.lang.reflect.Method;

/**
 * @author Leego Yih
 */
public class Main {
    static {
        try {
            addOffsetImplClass();
            addCameraKeyListenerClass();
            addCameraChatCommands();
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    static void addOffsetImplClass() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = classPool.makeClass("com.spiralstudio.mod.camera.OffsetImpl");
        ctClass.addInterface(classPool.get("com.threerings.opengl.a.b$a"));

        for (String name : new String[]{"_x", "_y", "_z"}) {
            CtField ctField = new CtField(classPool.get("float"), name, ctClass);
            ctField.setModifiers(Modifier.PUBLIC);
            ctClass.addField(ctField);
        }

        CtConstructor ctConstructor = new CtConstructor(classPool.get(new String[]{"float", "float", "float"}), ctClass);
        ctConstructor.setBody("{this._x=$1;this._y=$2;this._z=$3;}");
        ctConstructor.setModifiers(Modifier.PUBLIC);
        ctClass.addConstructor(ctConstructor);

        ctClass.addMethod(CtMethod.make("" +
                "public boolean apply(com.threerings.math.Transform3D o) {\n" +
                "    o.mL().h(this._x, this._y, this._z);\n" +
                "    return true;\n" +
                "}", ctClass));

        ctClass.toClass();
        ctClass.detach();
    }

    static void addCameraKeyListenerClass() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass2 = classPool.makeClass("com.spiralstudio.mod.camera.CameraKeyListener");
        ctClass2.addInterface(classPool.get("com.threerings.opengl.gui.event.e"));

        CtField ctField2Ctx = new CtField(classPool.get("com.threerings.projectx.util.A"), "_ctx", ctClass2);
        ctField2Ctx.setModifiers(Modifier.PUBLIC);
        ctClass2.addField(ctField2Ctx);

        CtField ctField2Old = new CtField(classPool.get("com.threerings.math.Vector3f"), "_old", ctClass2);
        ctField2Old.setModifiers(Modifier.PUBLIC);
        ctClass2.addField(ctField2Old);

        CtConstructor ctConstructor2 = new CtConstructor(classPool.get(new String[]{"com.threerings.projectx.util.A"}), ctClass2);
        ctConstructor2.setBody("{this._ctx=$1;this._old = new com.threerings.math.Vector3f();}");
        ctConstructor2.setModifiers(Modifier.PUBLIC);
        ctClass2.addConstructor(ctConstructor2);

        ctClass2.addMethod(CtMethod.make("" +
                "public void keyPressed(com.threerings.opengl.gui.event.KeyEvent event) {\n" +
                "            int code = event.getKeyCode();\n" +
                "            boolean repeat = event.isRepeat();\n" +
                "            System.out.println(\"keyPressed: code=\" + Integer.toString(code) + \", repeat=\" + repeat);\n" +
                "            int dx = 0;\n" +
                "            int dy = 0;\n" +
                "            int dz = 0;\n" +
                "            boolean up = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_UP);\n" +
                "            boolean down = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_DOWN);\n" +
                "            boolean left = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LEFT);\n" +
                "            boolean right = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_RIGHT);\n" +
                "            if (up != down) {\n" +
                "                if (up) {\n" +
                "                    dy += 1;\n" +
                "                } else {\n" +
                "                    dy -= 1;\n" +
                "                }\n" +
                "            }\n" +
                "            if (left != right) {\n" +
                "                if (left) {\n" +
                "                    dx -= 1;\n" +
                "                } else {\n" +
                "                    dx += 1;\n" +
                "                }\n" +
                "            }\n" +
                "            if (dx == 0 && dy == 0 && dz == 0) {\n" +
                "                return;\n" +
                "            }\n" +
                "            _old.x += dx;\n" +
                "            _old.y += dy;\n" +
                "            _old.z += dz;\n" +
                "            System.out.println(\"[CameraKeyListener] Move: dx=\" + Integer.toString(dx) + \", dy=\" + Integer.toString(dy) + \", dz=\" + Integer.toString(dz));\n" +
                "            com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)\n" +
                "                    java.lang.Class.forName(\"com.spiralstudio.mod.camera.OffsetImpl\")\n" +
                "                            .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})\n" +
                "                            .newInstance(new java.lang.Object[]{java.lang.Float.valueOf((float) dx), java.lang.Float.valueOf((float) dy), java.lang.Float.valueOf(0F)});\n" +
                "            com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;\n" +
                "            glapp.getCameraHandler().addOffset(offset);\n" +
                "        }", ctClass2));
        ctClass2.addMethod(CtMethod.make("" +
                "        public void keyReleased(com.threerings.opengl.gui.event.KeyEvent event) {" +
                "        }", ctClass2));
        ctClass2.addMethod(CtMethod.make("" +
                "        public void rollback() {\n" +
                "            float dx = -this._old.x;\n" +
                "            float dy = -this._old.y;\n" +
                "            float dz = -this._old.z;\n" +
                "            System.out.println(\"[CameraKeyListener] Rollback: dx=\" + Float.toString(dx) + \", dy=\" + Float.toString(dy) + \", dz=\" + Float.toString(dz));\n" +
                "            if (dx == 0 && dy == 0 && dz == 0) {\n" +
                "                return;\n" +
                "            }\n" +
                "            com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)\n" +
                "                    java.lang.Class.forName(\"com.spiralstudio.mod.camera.OffsetImpl\")\n" +
                "                            .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})\n" +
                "                            .newInstance(new java.lang.Object[]{java.lang.Float.valueOf(dx), java.lang.Float.valueOf(dy), java.lang.Float.valueOf(dz)});\n" +
                "            com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;\n" +
                "            glapp.getCameraHandler().addOffset(offset);\n" +
                "            this._old = new com.threerings.math.Vector3f();\n" +
                "        }", ctClass2));
        ctClass2.toClass();
        ctClass2.detach();
    }

    static void addCameraChatCommands() throws Exception {
        Method addField = Class.forName("com.spiralstudio.mod.command.Command")
                .getDeclaredMethod("addField", String.class, String.class);
        Method addCommand = Class.forName("com.spiralstudio.mod.command.Command")
                .getDeclaredMethod("addCommand", String.class, String.class);
        // Add a field for caching KeyListener
        addField.invoke(null, "_cameraKeyListener", "com.threerings.opengl.gui.event.e");
        // Add a command "/camon"
        addCommand.invoke(null, "camon", "" +
                "System.out.println(\"Camera On\");\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "this._cameraKeyListener = (com.threerings.opengl.gui.event.e)\n" +
                "        java.lang.Class.forName(\"com.spiralstudio.mod.camera.CameraKeyListener\")\n" +
                "                .getDeclaredConstructor(new Class[]{com.threerings.projectx.util.A.class})\n" +
                "                .newInstance(new Object[]{ctx__});\n" +
                "hud__.vk().Oh().addListener$2eebd3b8(this._cameraKeyListener);\n");
        // Add a command "/camoff"
        addCommand.invoke(null, "camoff", "" +
                "System.out.println(\"Camera Off\");\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "if (this._cameraKeyListener != null) {\n" +
                "    hud__.vk().Oh().removeListener$2eebd3b4(this._cameraKeyListener);\n" +
                "    java.lang.Class.forName(\"com.spiralstudio.mod.camera.CameraKeyListener\")\n" +
                "            .getDeclaredMethod(\"rollback\", new Class[0])\n" +
                "            .invoke(this._cameraKeyListener, new Object[0]);\n" +
                "    this._cameraKeyListener = null;\n" +
                "}\n");

/*System.out.println("Camera On");
com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);
this._cameraKeyListener = (com.threerings.opengl.gui.event.e)
        java.lang.Class.forName("com.spiralstudio.mod.camera.CameraKeyListener")
                .getDeclaredConstructor(new Class[]{com.threerings.projectx.util.A.class})
                .newInstance(new Object[]{ctx__});
hud__.vk().Oh().addListener$2eebd3b8(this._cameraKeyListener);*/

/*System.out.println("Camera Off");
com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);
if (this._cameraKeyListener != null) {
    hud__.vk().Oh().removeListener$2eebd3b4(this._cameraKeyListener);
    java.lang.Class.forName("com.spiralstudio.mod.camera.CameraKeyListener")
            .getDeclaredMethod("rollback", new Class[0])
            .invoke(this._cameraKeyListener, new Object[0]);
    this._cameraKeyListener = null;
}*/
    }

    /*class CameraKeyListener implements com.threerings.opengl.gui.event.e {
        public com.threerings.projectx.util.A _ctx;
        public com.threerings.math.Vector3f _old;

        public CameraKeyListener(com.threerings.projectx.util.A a) {
            this._ctx = a;
            this._old = new com.threerings.math.Vector3f();
        }

        public void keyPressed(com.threerings.opengl.gui.event.KeyEvent event) {
            int code = event.getKeyCode();
            boolean repeat = event.isRepeat();
            System.out.println("keyPressed: code=" + Integer.toString(code) + ", repeat=" + repeat);
            int dx = 0;
            int dy = 0;
            int dz = 0;
            boolean up = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_UP);
            boolean down = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_DOWN);
            boolean left = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LEFT);
            boolean right = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_RIGHT);
            if (up != down) {
                if (up) {
                    dy += 1;
                } else {
                    dy -= 1;
                }
            }
            if (left != right) {
                if (left) {
                    dx -= 1;
                } else {
                    dx += 1;
                }
            }
            if (dx == 0 && dy == 0 && dz == 0) {
                return;
            }
            _old.x += dx;
            _old.y += dy;
            _old.z += dz;
            System.out.println("[CameraKeyListener] Move: dx=" + Float.toString(dx) + ", dy=" + Float.toString(dy) + ", dz=" + Float.toString(dz));
            com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)
                    java.lang.Class.forName("com.spiralstudio.mod.camera.OffsetImpl")
                            .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})
                            .newInstance(new java.lang.Object[]{java.lang.Float.valueOf((float) dx), java.lang.Float.valueOf((float) dy), java.lang.Float.valueOf(0F)});
            com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;
            glapp.getCameraHandler().addOffset(offset);
        }

        public void keyReleased(com.threerings.opengl.gui.event.KeyEvent event) {
        }

        public void rollback() {
            float dx = -this._old.x;
            float dy = -this._old.y;
            float dz = -this._old.z;
            System.out.println("[CameraKeyListener] Rollback: dx=" + Float.toString(dx) + ", dy=" + Float.toString(dy) + ", dz=" + Float.toString(dz));
            if (dx == 0 && dy == 0 && dz == 0) {
                return;
            }
            com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)
                    java.lang.Class.forName("com.spiralstudio.mod.camera.OffsetImpl")
                            .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})
                            .newInstance(new java.lang.Object[]{java.lang.Float.valueOf(dx), java.lang.Float.valueOf(dy), java.lang.Float.valueOf(dz)});
            com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;
            glapp.getCameraHandler().addOffset(offset);
            this._old = new com.threerings.math.Vector3f();
        }
    }*/

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
