package com.spiralstudio.mod.core;

import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enter "/xhelp" to learn more about each command.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.chat.ProjectXChatDirector
 * @see com.threerings.crowd.chat.client.a ChatDirector
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 */
public class Commands {
    private static final Map<String, String> fields = new LinkedHashMap<>();
    private static final Map<String, String> commands = new LinkedHashMap<>();

    /**
     * Adds a custom field to {@code ChatDirector}, which can be accessed through {@code this}.
     *
     * @param fieldName the name of the field to be added.
     * @param fieldType the type of the field to be added.
     */
    public static void addField(String fieldName, String fieldType) {
        synchronized (fields) {
            fields.put(fieldName, fieldType);
        }
    }

    /**
     * Adds custom fields to {@code ChatDirector}, which can be accessed through {@code this}.
     *
     * @param map the fields to be added.
     */
    public static void addFields(Map<String, String> map) {
        synchronized (fields) {
            fields.putAll(map);
        }
    }

    /**
     * Adds a custom command to {@code ChatDirector#requestChat}.
     *
     * @param commandName the name of the command to be added.
     * @param commandBody the body of the command to be added.
     */
    public static void addCommand(String commandName, String commandBody) {
        synchronized (commands) {
            commands.put(commandName, commandBody);
        }
    }

    /**
     * Adds custom commands to {@code ChatDirector#requestChat}.
     *
     * @param map the commands to be added.
     */
    public static void addCommands(Map<String, String> map) {
        synchronized (commands) {
            commands.putAll(map);
        }
    }

    public static void init() throws Exception {
        // Do nothing if no fields and commands need to be added
        if (fields.isEmpty() && commands.isEmpty()) {
            return;
        }
        // Override class 'com.threerings.crowd.chat.client.ChatDirector'
        ClassBuilder.fromClass("com.threerings.crowd.chat.client.a")
                // Add custom fields
                .addFields(fields.entrySet()
                        .stream()
                        .map(e -> new FieldBuilder()
                                .fieldName(e.getKey())
                                .typeName(e.getValue())
                                .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT))
                        .collect(Collectors.toList()))
                // Add custom commands, override method 'com.threerings.crowd.chat.client.ChatDirector.requestChat'
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .paramTypeNames("com.threerings.crowd.chat.client.m", "java.lang.String", "boolean")
                        .insertBefore(buildCommandBody(commands)))
                .build();
    }

    static String buildCommandBody(Map<String, String> commands) {
        StringBuilder body = new StringBuilder();
        StringBuilder help = new StringBuilder();
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (name.indexOf('|') > 0) {
                StringBuilder condition = new StringBuilder();
                String[] alias = name.split("\\|");
                for (int i = 0; i < alias.length; i++) {
                    String cmd = alias[i].replace("/", "");
                    condition.append("$2.equalsIgnoreCase(\"/").append(cmd).append("\")");
                    if (i < alias.length - 1) {
                        condition.append(" || ");
                    }
                    help.append("/").append(cmd).append(" ");
                }
                body.append("if (").append(condition).append(") ");
            } else {
                String cmd = name.replace("/", "");
                help.append("/").append(cmd).append(" ");
                body.append("if ($2.equalsIgnoreCase(\"/").append(cmd).append("\")) ");
            }
            body.append("\n{ ").append(value).append("\nreturn \"success\"; }\n");
        }
        // Add '/xhelp'
        body.append("if ($2.equalsIgnoreCase(\"/xhelp\")) { \nreturn \"Available commands: ").append(help.toString()).append("\"; }\n");
        System.out.println("[Commands] Available commands: " + help.toString());
        /*System.out.println("[Commands] Generated: ---------------------------------------------------\n");
        System.out.println(body.toString());
        System.out.println("\n[Commands] End: ---------------------------------------------------\n");*/
        return body.toString();
    }
}
