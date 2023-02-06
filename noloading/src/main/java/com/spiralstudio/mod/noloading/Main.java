package com.spiralstudio.mod.noloading;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * @author Leego Yih
 * @see com.threerings.tudey.a.k
 * @see com.threerings.projectx.client.et
 */
public class Main {
    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.tudey.a.k");
            CtMethod ctMethod = ctClass.getDeclaredMethod("c", classPool.get(new String[]{"com.threerings.crowd.data.PlaceObject"}));
            ctMethod.setBody("{\n" +
                    "$0.aTX = (com.threerings.tudey.data.TudeySceneObject)$1;\n" +
                    "$0.aqE.rs().a($0);\n" +
                    "$0.aqE.rt().a($0);\n" +
                    "com.threerings.tudey.data.TudeySceneModel var2 = (com.threerings.tudey.data.TudeySceneModel)$0.aqE.ru().QY().Rf();\n" +
                    "$0.c(var2);\n" +
                    "}");
            /*CtClass ctClass = classPool.get("com.threerings.projectx.client.et");
            CtMethod ctMethod = ctClass.getDeclaredMethod("ul");
            ctMethod.setBody("{return null;}");*/
            ctClass.toClass();
            ctClass.detach();
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
