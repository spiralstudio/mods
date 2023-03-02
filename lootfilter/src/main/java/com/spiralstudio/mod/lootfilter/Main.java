package com.spiralstudio.mod.lootfilter;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Configs;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 * @see com.threerings.presents.dobj.MessageEvent
 * @see com.threerings.projectx.dungeon.client.a.A PickupSprite
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
        boolean added = addLootFilterClass(config);
        if (!added) {
            return;
        }
        redefinePickupSprite();
        redefineMessageEvent();
    }

    static boolean addLootFilterClass(Config config) throws Exception {
        if (config == null) {
            return false;
        }
        Config.Pickup pickup = config.getPickup();
        Config.Message message = config.getMessage();
        if (pickup == null && message == null) {
            return false;
        }
        Set<String>[] args = new Set[]{
                pickup != null && pickup.getType() != null ? pickup.getType().getExcluded() : null,
                pickup != null && pickup.getType() != null ? pickup.getType().getIncluded() : null,
                pickup != null && pickup.getName() != null ? pickup.getName().getExcluded() : null,
                pickup != null && pickup.getName() != null ? pickup.getName().getIncluded() : null,
                message != null && message.getType() != null ? message.getType().getExcluded() : null,
                message != null && message.getType() != null ? message.getType().getIncluded() : null,
                message != null && message.getName() != null ? message.getName().getExcluded() : null,
                message != null && message.getName() != null ? message.getName().getIncluded() : null,
        };
        for (int i = 0; i < args.length; i++) {
            if (args[i] == null) {
                args[i] = Collections.emptySet();
            } else {
                args[i] = args[i].stream()
                        .filter(o -> o != null && o.length() > 0)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
            }
        }
        ClassPool.make("com.spiralstudio.mod.lootfilter.LootFilter")
                .addField(new FieldBuilder()
                        .fieldName("_inited")
                        .typeName("boolean")
                        .modifiers(Modifier.PRIVATE | Modifier.STATIC | Modifier.VOLATILE))
                .addFields(Arrays.stream(new String[]{
                                "_pickupExcludedTypes", "_pickupIncludedTypes", "_pickupExcludedNames", "_pickupIncludedNames",
                                "_messageExcludedTypes", "_messageIncludedTypes", "_messageExcludedNames", "_messageIncludedNames"})
                        .map(e -> new FieldBuilder()
                                .fieldName(e)
                                .typeName("java.util.Set")
                                .modifiers(Modifier.PRIVATE | Modifier.STATIC))
                        .collect(Collectors.toList()))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public static void init(java.lang.Object[] args) {\n" +
                                "    if (args.length != 8) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    if (_inited) {\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    synchronized (com.spiralstudio.mod.lootfilter.LootFilter.class) {\n" +
                                "        if (_inited) {\n" +
                                "            return;\n" +
                                "        }\n" +
                                "        _inited = true;\n" +
                                "        _pickupExcludedTypes = (java.util.Set) args[0];\n" +
                                "        _pickupIncludedTypes = (java.util.Set) args[1];\n" +
                                "        _pickupExcludedNames = (java.util.Set) args[2];\n" +
                                "        _pickupIncludedNames = (java.util.Set) args[3];\n" +
                                "        _messageExcludedTypes = (java.util.Set) args[4];\n" +
                                "        _messageIncludedTypes = (java.util.Set) args[5];\n" +
                                "        _messageExcludedNames = (java.util.Set) args[6];\n" +
                                "        _messageIncludedNames = (java.util.Set) args[7];\n" +
                                "    }\n" +
                                "}"))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public static boolean isPickupExcluded(String type, String name) {\n" +
                                "    if (name != null) {\n" +
                                "        String nameLowerCase = name.toLowerCase();\n" +
                                "        if (_pickupIncludedNames.contains(nameLowerCase)) {\n" +
                                "            return false;\n" +
                                "        } else if (_pickupExcludedNames.contains(nameLowerCase)) {\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "    }\n" +
                                "    if (type != null) {\n" +
                                "        String typeLowerCase = type.toLowerCase();\n" +
                                "        if (_pickupIncludedTypes.contains(typeLowerCase)) {\n" +
                                "            return false;\n" +
                                "        } else if (_pickupExcludedTypes.contains(typeLowerCase)) {\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "    }\n" +
                                "    return false;\n" +
                                "}"))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public static boolean isMessageExcluded(String type, String name) {\n" +
                                "    if (name != null) {\n" +
                                "        String nameLowerCase = name.toLowerCase();\n" +
                                "        if (_messageIncludedNames.contains(nameLowerCase)) {\n" +
                                "            return false;\n" +
                                "        } else if (_messageExcludedNames.contains(nameLowerCase)) {\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "    }\n" +
                                "    if (type != null) {\n" +
                                "        String typeLowerCase = type.toLowerCase();\n" +
                                "        if (_messageIncludedTypes.contains(typeLowerCase)) {\n" +
                                "            return false;\n" +
                                "        } else if (_messageExcludedTypes.contains(typeLowerCase)) {\n" +
                                "            return true;\n" +
                                "        }\n" +
                                "    }\n" +
                                "    return false;\n" +
                                "}"))
                .actionOnComplete(clazz -> {
                    try {
                        clazz.getDeclaredMethod("init", new Class[]{Object[].class}).invoke(null, new Object[]{args});
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return true;
    }

    static void redefinePickupSprite() {
        // Override class `com.threerings.projectx.dungeon.client.a.PickupSprite`
        ClassPool.from("com.threerings.projectx.dungeon.client.a.A")
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .paramTypeNames("com.threerings.tudey.config.ActorSpriteConfig")
                        .insertAfter("" +
                                "if (this.Zj != null) {\n" +
                                "    com.threerings.projectx.util.A _ctx = (com.threerings.projectx.util.A) this.aqE;\n" +
                                "    com.threerings.projectx.item.data.Item _item = this.Zj;\n" +
                                "    String _name = _item.b(_ctx);\n" +
                                "    String _type = null;\n" +
                                "    com.threerings.projectx.item.config.ItemConfig.Original _original = _item.y(_ctx.getConfigManager());\n" +
                                "    if (_original != null) {\n" +
                                "        com.threerings.projectx.item.data.ItemCodes.Group _group = _original.BA();\n" +
                                "        if (_group != null) {\n" +
                                "            com.threerings.util.N _bundle = _ctx.getMessageManager().dI(\"item\");\n" +
                                "            if (_bundle != null) {\n" +
                                "                _type = _bundle.bu(_group.toString());\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }\n" +
                                "    Boolean _excluded = (Boolean) Class.forName(\"com.spiralstudio.mod.lootfilter.LootFilter\")\n" +
                                "        .getDeclaredMethod(\"isPickupExcluded\", new java.lang.Class[]{java.lang.String.class, java.lang.String.class})\n" +
                                "        .invoke(null, new java.lang.Object[]{_type, _name});" +
                                "    if (_excluded.booleanValue()) {\n" +
                                "        this._model.setVisible(false);\n" +
                                "        this._model.clearConfig();\n" +
                                "        super.Eo();\n" +
                                "    }\n" +
                                "}"));
    }

    static void redefineMessageEvent() {
        // Override class `com.threerings.presents.dobj.MessageEvent`
        ClassPool.from("com.threerings.presents.dobj.MessageEvent")
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
                                "    String _itemType = _bundle.bu(ss[1]);\n" +
                                "    String _itemName = _bundle.bu(ss[2]);\n" +
                                "    Boolean _excluded = (Boolean) Class.forName(\"com.spiralstudio.mod.lootfilter.LootFilter\")\n" +
                                "        .getDeclaredMethod(\"isMessageExcluded\", new java.lang.Class[]{java.lang.String.class, java.lang.String.class})\n" +
                                "        .invoke(null, new java.lang.Object[]{_itemType, _itemName});\n" +
                                "    return _excluded.booleanValue();\n" +
                                "}"));
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

    public static void main(String[] args) {
    }
}
