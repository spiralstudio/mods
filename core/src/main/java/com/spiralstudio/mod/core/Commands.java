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
import java.util.Arrays;
import java.util.Collection;
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
                // Add loot msg fields
                .addFields(Arrays.stream(new String[]{
                                "__lootMsgExcludedTypes",
                                "__lootMsgIncludedTypes",
                                "__lootMsgExcludedNames",
                                "__lootMsgIncludedNames"})
                        .map(e -> new FieldBuilder()
                                .fieldName(e)
                                .typeName("java.util.HashSet")
                                .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT | Modifier.VOLATILE))
                        .collect(Collectors.toList()))
                // Add custom commands, override method 'com.threerings.crowd.chat.client.ChatDirector.requestChat'
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .paramTypeNames("com.threerings.crowd.chat.client.m", "java.lang.String", "boolean")
                        .insertBefore(buildCommands(commands)))
                // Filter out loot messages
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .paramTypeNames("com.threerings.presents.dobj.MessageEvent")
                        .insertBefore(buildLootMsgFilter()))
                .build();
    }

    static String buildCommands(Map<String, String> commands) throws Exception {
        // Read custom configuration
        CmdConfig config = readConfig("cmd", CmdConfig.class);
        Map<String, String> aliasMap = config != null && config.getAlias() != null ? config.getAlias() : Collections.emptyMap();
        Set<String> disableSet = config != null && config.getDisable() != null ? config.getDisable() : Collections.emptySet();
        // Build commands body
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

    static String buildLootMsgFilter() throws Exception {
        // Read custom configuration
        LootMsgConfig config = readConfig("lootmsg", LootMsgConfig.class);
        if (config == null) {
            return "";
        }
        LootMsgConfig.Filter type = config.getType();
        LootMsgConfig.Filter name = config.getName();
        if (type == null && name == null) {
            return "";
        }
        String s1 = buildLootMsgStringArray(type != null ? type.getExcluded() : null);
        String s2 = buildLootMsgStringArray(type != null ? type.getIncluded() : null);
        String s3 = buildLootMsgStringArray(name != null ? name.getExcluded() : null);
        String s4 = buildLootMsgStringArray(name != null ? name.getIncluded() : null);
        return "if (\"crowd.chat\".equals($1.getName())) {\n" +
                "    if (this.__lootMsgExcludedTypes == null) {\n" +
                "        synchronized (this) {\n" +
                "            if (this.__lootMsgExcludedTypes == null) {\n" +
                "                this.__lootMsgExcludedTypes = new java.util.HashSet();\n" +
                "                java.util.Collections.addAll(this.__lootMsgExcludedTypes, ((Object[]) " + s1 + "));\n" +
                "            }\n" +
                "            if (this.__lootMsgIncludedTypes == null) {\n" +
                "                this.__lootMsgIncludedTypes = new java.util.HashSet();\n" +
                "                java.util.Collections.addAll(this.__lootMsgIncludedTypes, ((Object[]) " + s2 + "));\n" +
                "            }\n" +
                "            if (this.__lootMsgExcludedNames == null) {\n" +
                "                this.__lootMsgExcludedNames = new java.util.HashSet();\n" +
                "                java.util.Collections.addAll(this.__lootMsgExcludedNames, ((Object[]) " + s3 + "));\n" +
                "            }\n" +
                "            if (this.__lootMsgIncludedNames == null) {\n" +
                "                this.__lootMsgIncludedNames = new java.util.HashSet();\n" +
                "                java.util.Collections.addAll(this.__lootMsgIncludedNames, ((Object[]) " + s4 + "));\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    java.lang.Object[] _args = $1.getArgs();\n" +
                "    if (_args != null && _args.length > 0 && _args[0] != null && _args[0] instanceof com.threerings.crowd.chat.data.ChatMessage) {\n" +
                "        com.threerings.crowd.chat.data.ChatMessage _message = (com.threerings.crowd.chat.data.ChatMessage) _args[0];\n" +
                "        if (_message.bundle != null && _message.message != null && _message.bundle.equals(\"dungeon\") && _message.message.startsWith(\"m.item_awarded\")) {\n" +
                "            String[] _args = _message.message.split(\"\\\\|\");\n" +
                "            if (_args.length == 3) {\n" +
                "                com.threerings.util.N _bundle = this._ctx.getMessageManager().dI(_message.bundle);\n" +
                "                if (_bundle != null) {\n" +
                "                    boolean checkNext = true;\n" +
                "                    String itemType = _bundle.bu(_args[1]);\n" +
                "                    String itemName = _bundle.bu(_args[2]);\n" +
                "                    if (itemName != null) {\n" +
                "                        itemName = itemName.toLowerCase();\n" +
                "                        if (this.__lootMsgIncludedNames.contains(itemName)) {\n" +
                "                            checkNext = false;\n" +
                "                        } else if (this.__lootMsgExcludedNames.contains(itemName)) {\n" +
                "                            return;\n" +
                "                        }\n" +
                "                    }\n" +
                "                    if (checkNext && itemType != null) {\n" +
                "                        itemType = itemType.toLowerCase();\n" +
                "                        if (this.__lootMsgIncludedTypes.contains(itemType)) {\n" +
                "                        } else if (this.__lootMsgExcludedTypes.contains(itemType)) {\n" +
                "                            return;\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    static String buildLootMsgStringArray(Collection<String> c) {
        if (c == null) {
            return "new String[0]";
        }
        StringBuilder sb = new StringBuilder("new String[]{");
        for (String s : c) {
            sb.append("\"").append(s.toLowerCase()).append("\",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    static <T> T readConfig(String nane, Class<T> clazz) throws IOException {
        String dir = System.getProperty("user.dir");
        File file = new File(dir + "/code-mods/" + nane + ".yml");
        if (!file.exists()) {
            file = new File(dir + "/" + nane + ".yml");
        }
        if (!file.exists()) {
            return null;
        }
        try (InputStream is = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            return yaml.loadAs(is, clazz);
        }
    }

    public static class CmdConfig {
        private Map<String, String> alias;
        private Set<String> disable;

        public CmdConfig() {
        }

        public CmdConfig(Map<String, String> alias, Set<String> disable) {
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

    public static class LootMsgConfig {
        private Filter type;
        private Filter name;

        public LootMsgConfig() {
        }

        public LootMsgConfig(Filter type, Filter name) {
            this.type = type;
            this.name = name;
        }

        public Filter getType() {
            return type;
        }

        public void setType(Filter type) {
            this.type = type;
        }

        public Filter getName() {
            return name;
        }

        public void setName(Filter name) {
            this.name = name;
        }

        public static class Filter {
            private Set<String> excluded;
            private Set<String> included;

            public Filter() {
            }

            public Filter(Set<String> excluded, Set<String> included) {
                this.excluded = excluded;
                this.included = included;
            }

            public Set<String> getExcluded() {
                return excluded;
            }

            public void setExcluded(Set<String> excluded) {
                this.excluded = excluded;
            }

            public Set<String> getIncluded() {
                return included;
            }

            public void setIncluded(Set<String> included) {
                this.included = included;
            }
        }
    }

    public static void main(String[] args) {
    }
}
