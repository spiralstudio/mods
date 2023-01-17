package com.spiralstudio.mod.pandorabox;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.item.client.ArsenalPanel
 */
public class Main {
    static {
        System.out.println("PandoraBox initializing");
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.projectx.item.client.ArsenalPanel");
            CtMethod ctMethod = ctClass.getDeclaredMethod("a", new CtClass[]{classPool.get("com.threerings.presents.dobj.ElementUpdatedEvent")});
            ctMethod.setBody("{\n" +
                    "        System.out.println(\"[PandoraBox#elementUpdated] \" + $1.toString());\n" +
                    "        if ($1.getName().equals(\"equipment\")) {\n" +
                    "            Item item;\n" +
                    "            if ((item = (Item)this.asT.BZ().f((Long)$1.getOldValue())) != null) {\n" +
                    "                this.b(item, false);\n" +
                    "            }\n" +
                    "            if ((item = (Item)this.asT.BZ().f($1.qm())) != null) {\n" +
                    "                this.b(item, false);\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }");
            ctClass.toClass();
            ctClass.detach();
        } catch (Throwable cause) {
            throw new Error("Failed to load mod 'PandoraBox'", cause);
        }
        System.out.println("PandoraBox initialized");
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
