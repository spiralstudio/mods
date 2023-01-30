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
            CtMethod[] ctMethods = ctClass.getDeclaredMethods("a");
            for (CtMethod ctMethod : ctMethods) {
                CtClass[] pTypes = ctMethod.getParameterTypes();
                if (pTypes.length == 1) {
                    if (pTypes[0].getClass().getName().contains("ElementUpdatedEvent")) {
                        ctMethod.setBody("{\n" +
                                "        System.out.println(\"[PandoraBox#elementUpdated] \" + $1.toString());\n" +
                                "        if ($1.getName().equals(\"equipment\")) {\n" +
                                "            com.threerings.projectx.item.data.Item item;\n" +
                                "            if ((item = (com.threerings.projectx.item.data.Item)$0.asT.BZ().f((java.lang.Comparable)$1.getOldValue())) != null) {\n" +
                                "                $0.b(item, false);\n" +
                                "            }\n" +
                                "            if ((item = (com.threerings.projectx.item.data.Item)$0.asT.BZ().f((java.lang.Comparable)Long.valueOf($1.qm()))) != null) {\n" +
                                "                $0.b(item, false);\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }");
                        System.out.println("ArsenalPanel initialized");
                        break;
                    }
                }
            }
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
