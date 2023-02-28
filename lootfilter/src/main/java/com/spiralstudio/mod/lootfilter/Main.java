package com.spiralstudio.mod.lootfilter;

import com.spiralstudio.mod.core.Configs;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 * @see com.threerings.presents.dobj.MessageEvent
 */
public class Main {
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        Config config = Configs.read("lootfilter", Config.class);
        redefineMessageEvent(config);
    }

    static void redefineMessageEvent(Config config) throws Exception {
        if (config == null || config.getMessage() == null) {
            return;
        }
        if (config.getMessage().getType() == null && config.getMessage().getName() == null) {
            return;
        }
        Config.Message.Filter type = config.getMessage().getType();
        Config.Message.Filter name = config.getMessage().getName();
        Set<String> excludedTypes = type != null ? type.getExcluded() : null;
        Set<String> includedTypes = type != null ? type.getIncluded() : null;
        Set<String> excludedNames = name != null ? name.getExcluded() : null;
        Set<String> includedNames = name != null ? name.getIncluded() : null;
        // Override class `com.threerings.presents.dobj.MessageEvent`
        ClassBuilder.fromClass("com.threerings.presents.dobj.MessageEvent")
                .addFields(Arrays.stream(new String[]{
                                "__lootExcludedTypes",
                                "__lootIncludedTypes",
                                "__lootExcludedNames",
                                "__lootIncludedNames"})
                        .map(e -> new FieldBuilder()
                                .fieldName(e)
                                .typeName("java.util.HashSet")
                                .modifiers(Modifier.PUBLIC | Modifier.STATIC | Modifier.TRANSIENT | Modifier.VOLATILE))
                        .collect(Collectors.toList()))
                // Override method `com.threerings.presents.dobj.MessageEvent.notifyListener`
                .modifyMethod(new MethodModifier()
                        .methodName("aO")
                        .paramTypeNames("java.lang.Object")
                        .insertBefore("" +
                                "if ($1 instanceof com.threerings.crowd.chat.client.a) {\n" +
                                "    com.threerings.crowd.chat.client.a _cd = (com.threerings.crowd.chat.client.a) $1;\n" +
                                "    java.lang.reflect.Field _ctxField = com.threerings.presents.client.a.class.getDeclaredField(\"_ctx\");\n" +
                                "    _ctxField.setAccessible(true);\n" +
                                "    if (this.hitLootMessage((com.threerings.presents.b.b) _ctxField.get(_cd))) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "}"))
                // Filter out loot messages
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public boolean hitLootMessage(com.threerings.presents.b.b ctx) {\n" +
                                "    com.threerings.presents.dobj.MessageEvent event = this;\n" +
                                "    if (!\"crowd.chat\".equals(event.getName())) {\n" +
                                "        return false;\n" +
                                "    }\n" +
                                "    if (__lootExcludedTypes == null) {\n" +
                                "        synchronized (com.threerings.presents.dobj.MessageEvent.class) {\n" +
                                "            if (__lootExcludedTypes == null) {\n" +
                                "                __lootExcludedTypes = new java.util.HashSet();\n" +
                                "                __lootIncludedTypes = new java.util.HashSet();\n" +
                                "                __lootExcludedNames = new java.util.HashSet();\n" +
                                "                __lootIncludedNames = new java.util.HashSet();\n" +
                                "                java.util.Collections.addAll(__lootExcludedTypes, ((Object[]) " + buildLootStringArray(excludedTypes) + "));\n" +
                                "                java.util.Collections.addAll(__lootIncludedTypes, ((Object[]) " + buildLootStringArray(includedTypes) + "));\n" +
                                "                java.util.Collections.addAll(__lootExcludedNames, ((Object[]) " + buildLootStringArray(excludedNames) + "));\n" +
                                "                java.util.Collections.addAll(__lootIncludedNames, ((Object[]) " + buildLootStringArray(includedNames) + "));\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }\n" +
                                "    java.lang.Object[] _args = event.getArgs();\n" +
                                "    if (_args == null || _args.length == 0 || _args[0] == null || !(_args[0] instanceof com.threerings.crowd.chat.data.ChatMessage)) {\n" +
                                "        return false;\n" +
                                "    }\n" +
                                "    com.threerings.crowd.chat.data.ChatMessage _message = (com.threerings.crowd.chat.data.ChatMessage) _args[0];\n" +
                                "    if (_message.bundle == null || _message.message == null || !_message.bundle.equals(\"dungeon\") || !_message.message.startsWith(\"m.item_awarded\")) {\n" +
                                "        return false;\n" +
                                "    }\n" +
                                "    String[] ss = _message.message.split(\"\\\\|\");\n" +
                                "    if (ss.length != 3) {\n" +
                                "        return false;\n" +
                                "    }\n" +
                                "    com.threerings.util.N _bundle = ctx.getMessageManager().dI(_message.bundle);\n" +
                                "    if (_bundle == null) {\n" +
                                "        return false;\n" +
                                "    }\n" +
                                "    String itemType = _bundle.bu(ss[1]);\n" +
                                "    String itemName = _bundle.bu(ss[2]);\n" +
                                "    if (itemName != null) {\n" +
                                "        itemName = itemName.toLowerCase();\n" +
                                "        if (__lootIncludedNames.contains(itemName)) {\n" +
                                "            return false;\n" +
                                "        } else if (__lootExcludedNames.contains(itemName)) {\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "    }\n" +
                                "    if (itemType != null) {\n" +
                                "        itemType = itemType.toLowerCase();\n" +
                                "        if (__lootIncludedTypes.contains(itemType)) {\n" +
                                "            return false;\n" +
                                "        } else if (__lootExcludedTypes.contains(itemType)) {\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "    }\n" +
                                "    return true;\n" +
                                "}"))
                .build();
    }

    static String buildLootStringArray(Collection<String> c) {
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

    public static class Config {
        private Pickup pickup;
        private Message message;

        public Pickup getPickup() {
            return pickup;
        }

        public void setPickup(Pickup pickup) {
            this.pickup = pickup;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public static class Pickup {

        }

        public static class Message {
            private Filter type;
            private Filter name;

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
    }

    public static void main(String[] args) {
    }
}
