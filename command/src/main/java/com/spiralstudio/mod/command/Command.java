package com.spiralstudio.mod.command;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Type "/ah" to call the Auction House anytime and anywhere.
 *
 * @author Leego Yih
 * @see com.threerings.crowd.chat.client.a ChatDirector
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 */
public class Command {
    private static Map<String, String> commands = new HashMap<>();

    public static void add(String cmd, String action) {
        commands.put(cmd, action);
    }

    public static void mount() {
        if (commands.isEmpty()) {
            return;
        }
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.crowd.chat.client.a");
            CtMethod ctMethod = ctClass.getDeclaredMethod("a", classPool.get(new String[]{"com.threerings.crowd.chat.client.m", "java.lang.String", "boolean"}));
            for (Map.Entry<String, String> entry : commands.entrySet()) {
                String cmd = entry.getKey();
                String action = entry.getValue();
                ctMethod.insertBefore("if ($2.equals(\"/" + cmd + "\")) { " + action + "\nreturn \"success\"; }");
                System.out.println("Registered command [" + cmd + "]");
            }
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
