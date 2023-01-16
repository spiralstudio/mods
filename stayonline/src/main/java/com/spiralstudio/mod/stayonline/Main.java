package com.spiralstudio.mod.stayonline;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * Overrides the `start` method of the class `IdleTracker` to disable it.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.ProjectXApp.c aka IdleTracker
 */
public class Main {
    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.projectx.client.ProjectXApp$c");
            CtMethod ctMethod = ctClass.getDeclaredMethod("start");
            ctMethod.setBody("{System.out.println(\"Never Idle\");}");
            ctClass.toClass();
            ctClass.detach();
        } catch (Throwable cause) {
            throw new Error("Failed to load mod 'StayOnline'", cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
