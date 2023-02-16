package com.spiralstudio.mod.test;

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
            testMonsterHudBar();
            testAdminChatCommand();
            addCameraChatCommands();
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    static void testMonsterHudBar() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = classPool.get("com.threerings.projectx.dungeon.client.g");
        CtMethod ctMethod = ctClass.getDeclaredMethod("tick");
        ctMethod.insertAfter("this.aov.setVisible(true);\n");
        ctClass.toClass();
        ctClass.detach();
    }

    static void testAdminChatCommand() throws Exception {
        // Does anyone have a token greater than zero?
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = classPool.get("com.threerings.crowd.data.TokenRing");
        ctClass.getDeclaredConstructors()[0].insertBefore("System.out.println(\"token :\" + $1);");
        CtMethod ctMethod1 = ctClass.getDeclaredMethod("aw");
        ctMethod1.setBody("{return true;}");
        CtMethod ctMethod2 = ctClass.getDeclaredMethod("ax");
        ctMethod2.setBody("{return true;}");
        CtMethod ctMethod3 = ctClass.getDeclaredMethod("iD");
        ctMethod3.setBody("{return true;}");
        ctClass.toClass();
        ctClass.detach();
    }

    static void addCameraChatCommands() throws Exception {
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

        CtClass ctClass2 = classPool.makeClass("com.spiralstudio.mod.camera.CameraKeyListener");
        ctClass2.addInterface(classPool.get("com.threerings.opengl.gui.event.e"));

        CtField ctField2Ctx = new CtField(classPool.get("com.threerings.projectx.util.A"), "_ctx", ctClass2);
        ctField2Ctx.setModifiers(Modifier.PUBLIC);
        ctClass2.addField(ctField2Ctx);

        CtField ctField2Old = new CtField(classPool.get("com.threerings.math.Vector3f"), "_old", ctClass2);
        ctField2Old.setModifiers(Modifier.PUBLIC);
        ctClass2.addField(ctField2Old);

        CtField ctField2Flag = new CtField(classPool.get("boolean"), "_flag", ctClass2);
        ctField2Flag.setModifiers(Modifier.PUBLIC);
        ctClass2.addField(ctField2Flag);

        CtConstructor ctConstructor2 = new CtConstructor(classPool.get(new String[]{"com.threerings.projectx.util.A"}), ctClass2);
        ctConstructor2.setBody("{this._ctx=$1;}");
        ctConstructor2.setModifiers(Modifier.PUBLIC);
        ctClass2.addConstructor(ctConstructor2);

        ctClass2.addMethod(CtMethod.make("" +
                "public void keyPressed(com.threerings.opengl.gui.event.KeyEvent event) {\n" +
                "            int code = event.getKeyCode();\n" +
                "            boolean rpt = event.isRepeat();\n" +
                "            System.out.println(\"keyPressed: code=\" + Integer.toString(code) + \", rpt=\" + rpt);\n" +
                "            if (code == 14 && rpt) {\n" +
                "                //int dx = org.lwjgl.input.Mouse.getDX();\n" +
                "                //int dy = org.lwjgl.input.Mouse.getDY();\n" +
                "                int dx = 0;\n" +
                "                int dy = 0;\n" +
                "                int dz = 0;\n" +
                "                boolean up = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_UP);\n" +
                "                boolean down = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_DOWN);\n" +
                "                boolean left = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_LEFT);\n" +
                "                boolean right = org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_RIGHT);\n" +
                "                if (up != down) {\n" +
                "                    if (up) {\n" +
                "                        dy += 1;\n" +
                "                    } else {\n" +
                "                        dy -= 1;\n" +
                "                    }\n" +
                "                }\n" +
                "                if (left != right) {\n" +
                "                    if (left) {\n" +
                "                        dx -= 1;\n" +
                "                    } else {\n" +
                "                        dx += 1;\n" +
                "                    }\n" +
                "                }\n" +
                "                if (dx == 0 && dy == 0 && dz == 0) {\n" +
                "                    return;\n" +
                "                }\n" +
                "                _old.x += dx;\n" +
                "                _old.y += dy;\n" +
                "                _old.z += dz;\n" +
                "                System.out.println(\"dx=\" + Integer.valueOf(dx) + \", dy=\" + Integer.valueOf(dy));\n" +
                "                com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)\n" +
                "                        java.lang.Class.forName(\"com.spiralstudio.mod.camera.OffsetImpl\")\n" +
                "                                .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})\n" +
                "                                .newInstance(new java.lang.Object[]{java.lang.Float.valueOf((float) dx), java.lang.Float.valueOf((float) dy), java.lang.Float.valueOf(0F)});\n" +
                "                com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;\n" +
                "                glapp.getCameraHandler().addOffset(offset);\n" +
                "            }\n" +
                "        }", ctClass2));
        ctClass2.addMethod(CtMethod.make("" +
                "public void keyReleased(com.threerings.opengl.gui.event.KeyEvent event) {\n" +
                "            int code = event.getKeyCode();\n" +
                "            System.out.println(\"keyReleased: code=\" + Integer.toString(code));\n" +
                "            if (code == 14) {\n" +
                "                com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;\n" +
                "                float dx = -this._old.x;\n" +
                "                float dy = -this._old.y;\n" +
                "                float dz = -this._old.z;\n" +
                "                System.out.println(\"keyReleased: offset x=\" + Float.toString(dx) + \", y=\" + Float.toString(dy) + \", z=\" + Float.toString(dz));\n" +
                "                com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)\n" +
                "                        java.lang.Class.forName(\"com.spiralstudio.mod.camera.OffsetImpl\")\n" +
                "                                .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})\n" +
                "                                .newInstance(new java.lang.Object[]{java.lang.Float.valueOf(dx), java.lang.Float.valueOf(dy), java.lang.Float.valueOf(dz)});\n" +
                "                glapp.getCameraHandler().addOffset(offset);\n" +
                "                this._old = new com.threerings.math.Vector3f();\n" +
                "            }\n" +
                "        }", ctClass2));
        ctClass2.toClass();
        ctClass2.detach();


        Method addCommand = Class.forName("com.spiralstudio.mod.command.Command")
                .getDeclaredMethod("addCommand", String.class, String.class);
        addCommand.invoke(null, "forward", newOffset("0F", "1F", "0F"));
        addCommand.invoke(null, "backward", newOffset("0F", "-1F", "0F"));
        addCommand.invoke(null, "left", newOffset("-1F", "0F", "0F"));
        addCommand.invoke(null, "right", newOffset("1F", "0F", "0F"));
        addCommand.invoke(null, "up", newOffset("0F", "0F", "1F"));
        addCommand.invoke(null, "down", newOffset("0F", "0F", "-1F"));


        addCommand.invoke(null, "camon", "" +
                "System.out.println(\"camon\");\n" +
                "        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "        com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "        com.threerings.opengl.gui.event.e lis__ = (com.threerings.opengl.gui.event.e)\n" +
                "                java.lang.Class.forName(\"com.spiralstudio.mod.camera.CameraKeyListener\")\n" +
                "                        .getDeclaredConstructor(new Class[]{com.threerings.projectx.util.A.class})\n" +
                "                        .newInstance(new Object[]{ctx__});\n" +
                "        hud__.vk().Oh().addListener$2eebd3b8(lis__);\n");

        /*System.out.println("cam");
        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
        com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);
        com.threerings.opengl.gui.event.e lis__ = (com.threerings.opengl.gui.event.e)
                java.lang.Class.forName("com.spiralstudio.mod.camera.CameraKeyListener")
                        .getDeclaredConstructor(new Class[]{com.threerings.projectx.util.A.class})
                        .newInstance(new Object[]{ctx__});
        hud__.vk().Oh().addListener$2eebd3b8(lis__);*/

        addCommand.invoke(null, "camoff", "" +
                "System.out.println(\"camoff\");\n" +
                "        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "        com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "        com.threerings.opengl.gui.event.e lis__ = (com.threerings.opengl.gui.event.e)\n" +
                "                java.lang.Class.forName(\"com.spiralstudio.mod.camera.CameraKeyListener\")\n" +
                "                        .getDeclaredConstructor(new Class[]{com.threerings.projectx.util.A.class})\n" +
                "                        .newInstance(new Object[]{ctx__});\n" +
                "        hud__.vk().Oh().removeListener$2eebd3b4(lis__);");

        /*System.out.println("camoff");
        com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;
        com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);
        com.threerings.opengl.gui.event.e lis__ = (com.threerings.opengl.gui.event.e)
                java.lang.Class.forName("com.spiralstudio.mod.camera.CameraKeyListener")
                        .getDeclaredConstructor(new Class[]{com.threerings.projectx.util.A.class})
                        .newInstance(new Object[]{ctx__});
        hud__.vk().Oh().removeListener$2eebd3b4(lis__);*/

    }

    /*public boolean apply(com.threerings.math.Transform3D o) {
        o.mL().h(this.x, this.y, this.z);
        return true;
    }*/

    /*class CameraKeyListener {
        public com.threerings.projectx.util.A _ctx;
        public com.threerings.math.Vector3f _old;
        public boolean _flag = false;

        public CameraKeyListener(com.threerings.projectx.util.A a) {
            this._ctx = a;
            this._old = new com.threerings.math.Vector3f();
        }

        public void keyPressed(com.threerings.opengl.gui.event.KeyEvent event) {
            int code = event.getKeyCode();
            boolean rpt = event.isRepeat();
            System.out.println("keyPressed: code=" + Integer.toString(code) + ", rpt=" + rpt);
            if (code == 14 && rpt) {
                //int dx = org.lwjgl.input.Mouse.getDX();
                //int dy = org.lwjgl.input.Mouse.getDY();
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
                System.out.println("dx=" + Integer.valueOf(dx) + ", dy=" + Integer.valueOf(dy));
                com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)
                        java.lang.Class.forName("com.spiralstudio.mod.camera.OffsetImpl")
                                .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})
                                .newInstance(new java.lang.Object[]{java.lang.Float.valueOf((float) dx), java.lang.Float.valueOf((float) dy), java.lang.Float.valueOf(0F)});
                com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;
                glapp.getCameraHandler().addOffset(offset);
            }
        }

        public void keyReleased(com.threerings.opengl.gui.event.KeyEvent event) {
            int code = event.getKeyCode();
            System.out.println("keyReleased: code=" + Integer.toString(code));
            if (code == 14) {
                com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;
                float dx = -this._old.x;
                float dy = -this._old.y;
                float dz = -this._old.z;
                System.out.println("keyReleased: offset x=" + Float.toString(dx) + ", y=" + Float.toString(dy) + ", z=" + Float.toString(dz));
                com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)
                        java.lang.Class.forName("com.spiralstudio.mod.camera.OffsetImpl")
                                .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})
                                .newInstance(new java.lang.Object[]{java.lang.Float.valueOf(dx), java.lang.Float.valueOf(dy), java.lang.Float.valueOf(dz)});
                glapp.getCameraHandler().addOffset(offset);
                this._old = new com.threerings.math.Vector3f();
            }
        }
    }*/

    static String newOffset(String x, String y, String z) {
        /*com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a)
                Class.forName("com.spiralstudio.mod.camera.OffsetImpl")
                .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})
                .newInstance(new Object[]{1, 2, 3});
        com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;
        glapp.getCameraHandler().addOffset(offset);*/

        return "com.threerings.opengl.a.b.a offset = (com.threerings.opengl.a.b.a) \n" +
                "        java.lang.Class.forName(\"com.spiralstudio.mod.camera.OffsetImpl\")\n" +
                "            .getDeclaredConstructor(new Class[]{float.class, float.class, float.class})\n" +
                "            .newInstance(new java.lang.Object[]{java.lang.Float.valueOf(" + x + "), java.lang.Float.valueOf(" + y + "), java.lang.Float.valueOf(" + z + ")});\n" +
                "        com.threerings.opengl.e glapp = (com.threerings.opengl.e) this._ctx;\n" +
                "        glapp.getCameraHandler().addOffset(offset);\n";
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
