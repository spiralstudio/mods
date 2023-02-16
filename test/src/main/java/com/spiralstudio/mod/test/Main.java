package com.spiralstudio.mod.test;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * @author Leego Yih
 */
public class Main {
    static {
        try {
            testMonsterHudBar();
            testAdminChatCommand();
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

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
