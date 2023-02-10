package com.spiralstudio.mod.command;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enter "/xhelp" to learn more about each command.
 *
 * @author Leego Yih
 * @see com.threerings.crowd.chat.client.a ChatDirector
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 */
public class Command {
    private static Map<String, String> commands = new LinkedHashMap<>();
    private static Map<String, String> fields = new LinkedHashMap<>();

    public static void addCommand(String cmd, String action) {
        synchronized (commands) {
            commands.put(cmd, action);
        }
    }

    public static void addField(String fieldName, String fieldType) {
        synchronized (commands) {
            fields.put(fieldName, fieldType);
        }
    }

    public static void mount() {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.crowd.chat.client.a");

            for (Map.Entry<String, String> entry : fields.entrySet()) {
                CtField ctField = new CtField(classPool.get(entry.getValue()), entry.getKey(), ctClass);
                ctField.setModifiers(Modifier.PUBLIC);
                ctClass.addField(ctField);
            }

            CtMethod ctMethod = ctClass.getDeclaredMethod("a", classPool.get(new String[]{"com.threerings.crowd.chat.client.m", "java.lang.String", "boolean"}));
            if (commands.isEmpty()) {
                ctMethod.insertBefore("if ($2.equalsIgnoreCase(\"/xhelp\")) { \nreturn \"No available commands.\"; }");
            } else {
                StringBuilder xhelp = new StringBuilder();
                for (Map.Entry<String, String> entry : commands.entrySet()) {
                    String cmd = entry.getKey();
                    String action = entry.getValue();
                    if (cmd.indexOf('|') > 0) {
                        StringBuilder filter = new StringBuilder();
                        String[] alias = cmd.split("\\|");
                        for (int i = 0; i < alias.length; i++) {
                            String s = alias[i];
                            filter.append("$2.equalsIgnoreCase(\"/").append(s).append("\")");
                            if (i < alias.length - 1) {
                                filter.append(" || ");
                            }
                            xhelp.append("/").append(s).append(" ");
                        }
                        ctMethod.insertBefore(String.format("if (%s) \n{ %s\nreturn \"success\"; }\n", filter, action));
                    } else {
                        ctMethod.insertBefore(String.format("if ($2.equalsIgnoreCase(\"/%s\")) \n{ %s\nreturn \"success\"; }\n", cmd, action));
                        xhelp.append("/").append(cmd).append(" ");
                    }
                }
                System.out.println("Available commands: " + xhelp.toString());
                ctMethod.insertBefore("if ($2.equalsIgnoreCase(\"/xhelp\")) { \nreturn \"Available commands: " + xhelp.toString() + "\"; }\n");
            }
            ctClass.toClass();
            ctClass.detach();
        } catch (Throwable cause) {
            System.out.println(cause.getMessage());
            throw new Error(cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
