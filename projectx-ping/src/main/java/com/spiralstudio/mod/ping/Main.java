package com.spiralstudio.mod.ping;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * Overrides the `notePing` method of the class `Minimap` to display ping value.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.hud.Minimap
 */
public class Main {

    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.projectx.client.hud.Minimap");
            CtMethod ctMethod = ctClass.getDeclaredMethod("bG");
            ctMethod.setBody("{$0.aoQ.setIcon(null);$0.aoQ.setText(Integer.toString($1) + \"ms\");}");
            ctClass.toClass();
            ctClass.detach();
        } catch (Throwable cause) {
            throw new Error("Failed to load mod 'Ping'", cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
