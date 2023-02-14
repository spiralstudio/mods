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
        // ProjectXChatDirector
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
