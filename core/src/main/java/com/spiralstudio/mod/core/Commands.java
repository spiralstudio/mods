package com.spiralstudio.mod.core;

import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        // Read custom configuration
        Config config = readConfig();
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
                        .insertBefore(buildCommands(commands, config)))
                .build();
    }

    static String buildCommands(Map<String, String> commands, Config config) {
        Map<String, String> aliasMap = config.getAlias() != null ? config.getAlias() : Collections.emptyMap();
        Set<String> disableSet = config.getDisable() != null ? config.getDisable() : Collections.emptySet();
        StringBuilder body = new StringBuilder();
        StringBuilder help = new StringBuilder();
        for (Map.Entry<String, String> entry : commands.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            List<String> cmds = new ArrayList<>();
            if (name.indexOf('|') > 0) {
                for (String s : name.split("\\|")) {
                    String cmd = s.replace("/", "");
                    if (!disableSet.contains(cmd)) {
                        cmds.add(cmd);
                    }
                    String alias = aliasMap.get(cmd);
                    if (alias != null) {
                        cmds.add(alias);
                    }
                }
            } else {
                String cmd = name.replace("/", "");
                if (!disableSet.contains(cmd)) {
                    cmds.add(cmd);
                }
                String alias = aliasMap.get(cmd);
                if (alias != null) {
                    cmds.add(alias);
                }
            }
            if (!cmds.isEmpty()) {
                buildCommand(body, help, cmds, value);
            }
        }
        // Add '/xhelp'
        body.append("if ($2.equalsIgnoreCase(\"/xhelp\")) { \nreturn \"Available commands: ").append(help.toString()).append("\"; }\n");
        System.out.println("[Commands] Available commands: " + help.toString());
        /*System.out.println("[Commands] Generated: ---------------------------------------------------\n");
        System.out.println(body.toString());
        System.out.println("\n[Commands] End: ---------------------------------------------------\n");*/
        return body.toString();
    }

    static void buildCommand(StringBuilder body, StringBuilder help, List<String> cmds, String src) {
        if (cmds.size() == 1) {
            String cmd = cmds.get(0);
            help.append("/").append(cmd).append(" ");
            body.append("if ($2.equalsIgnoreCase(\"/").append(cmd).append("\")) \n")
                    .append("{ ").append(src).append(" return \"success\"; }\n");
        } else if (cmds.size() > 1) {
            StringBuilder condition = new StringBuilder();
            for (int i = 0; i < cmds.size(); i++) {
                String cmd = cmds.get(i).replace("/", "");
                condition.append("$2.equalsIgnoreCase(\"/").append(cmd).append("\")");
                if (i < cmds.size() - 1) {
                    condition.append(" || ");
                }
                help.append("/").append(cmd).append(" ");
            }
            body.append("if (").append(condition).append(") \n")
                    .append("{ ").append(src).append(" return \"success\"; }\n");
        }
    }

    static Config readConfig() throws IOException {
        String dir = System.getProperty("user.dir");
        File file = new File(dir + "/code-mods/cmd.yml");
        if (!file.exists()) {
            file = new File(dir + "/cmd.yml");
        }
        if (!file.exists()) {
            return new Config();
        }
        try (InputStream is = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(is, Config.class);
        }
    }

    public static class Config {
        private Map<String, String> alias;
        private Set<String> disable;

        public Config() {
        }

        public Config(Map<String, String> alias, Set<String> disable) {
            this.alias = alias;
            this.disable = disable;
        }

        public Map<String, String> getAlias() {
            return alias;
        }

        public void setAlias(Map<String, String> alias) {
            this.alias = alias;
        }

        public Set<String> getDisable() {
            return disable;
        }

        public void setDisable(Set<String> disable) {
            this.disable = disable;
        }
    }

    public static void main(String[] args) {
    }
}
