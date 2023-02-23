package com.spiralstudio.mod.pandora;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorBuilder;
import com.spiralstudio.mod.core.util.FieldBuilder;
import com.spiralstudio.mod.core.util.MethodBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

import java.lang.reflect.Modifier;

/**
 * Enter "/pandora" to call the Pandora.
 * Enter "/clearpandora" to help GC.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.item.client.ArsenalPanel
 * @see com.threerings.projectx.shop.client.u VendorListPanel
 * @see com.threerings.projectx.shop.client.l ShopDialog
 * @see com.threerings.projectx.shop.data.ShopDialogInfo
 * @see com.threerings.projectx.shop.data.UniqueShopInfo
 * @see com.threerings.projectx.data.PlayerObject
 * @see com.threerings.crowd.client.d LocationDirector
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
        addPreviewApplyListenerClass();
        redefineKnightSpriteToReplacePreviewItems();
        redefineGoodSlotToIgnoreException();
        redefineVendorListPanelToIgnoreException();
        redefineShopDialogToCacheAndRenameTitle();
        redefinePlayerObjectToPreview();
        addPandoraCommand();
    }

    static void addPreviewApplyListenerClass() throws Exception {
        ClassBuilder.makeClass("com.spiralstudio.mod.pandora.PreviewApplyListener")
                .interfaceClassName("com.threerings.opengl.gui.event.a")
                .addField(new FieldBuilder()
                        .fieldName("_ctx")
                        .typeName("com.threerings.projectx.util.A")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT))
                .addConstructor(new ConstructorBuilder()
                        .parameters(new String[]{"com.threerings.projectx.util.A"})
                        .body("{this._ctx=$1; System.out.println(\"PreviewApplyListener New\");}")
                        .modifiers(Modifier.PUBLIC))
                .addMethod(new MethodBuilder()
                        .body("" +
                                "public void actionPerformed(com.threerings.opengl.gui.event.ActionEvent actionEvent) {\n" +
                                "    System.out.println(\"[Pandora] Apply actionPerformed:\" + actionEvent.toString());\n" +
                                "    com.threerings.opengl.gui.av _btn = (com.threerings.opengl.gui.av) actionEvent.getSource();\n" +
                                "    System.out.println(\"[Pandora] Apply\" + Boolean.toString(_btn.isSelected()));\n" +
                                "    if (_btn.isSelected()) {\n" +
                                "        System.out.println(\"Selected\");\n" +
                                "        com.threerings.projectx.client.dj whatever = this._ctx.xl();\n" +
                                "        java.lang.reflect.Field agdField = com.threerings.projectx.client.dj.class.getDeclaredField(\"agd\");\n" +
                                "        agdField.setAccessible(true);\n" +
                                "        com.threerings.projectx.data.PlayerEquipment playerEquipment = (com.threerings.projectx.data.PlayerEquipment) agdField.get(whatever);\n" +
                                "        java.lang.reflect.Field itemsField = com.threerings.projectx.data.PlayerEquipment.class.getDeclaredField(\"items\");\n" +
                                "        itemsField.setAccessible(true);\n" +
                                "        java.lang.Object itemsObject = itemsField.get(playerEquipment);\n" +
                                "        if (itemsObject == null) {\n" +
                                "            return;\n" +
                                "        }\n" +
                                "        java.util.Map itemsMap = (java.util.Map) itemsObject;\n" +
                                "        com.threerings.projectx.item.data.LevelItem[] previewItems = new com.threerings.projectx.item.data.LevelItem[16];\n" +
                                "        for (int i = 0; i < previewItems.length; i++) {\n" +
                                "            java.lang.Object itemObj = itemsMap.get(Integer.valueOf(i));\n" +
                                "            if (itemObj != null) {\n" +
                                "                previewItems[i] = (com.threerings.projectx.item.data.LevelItem) itemObj;\n" +
                                "                System.out.println(\"Item key=\" + Integer.valueOf(i) + \", value=\" + itemObj.toString());\n" +
                                "            }\n" +
                                "        }\n" +
                                "        com.threerings.projectx.data.PlayerObject playerObject = this._ctx.uk();\n" +
                                "\n" +
                                "        java.lang.reflect.Field aLsField = com.threerings.projectx.item.data.Item.class.getDeclaredField(\"aLs\");\n" +
                                "        java.lang.reflect.Field aLtField = com.threerings.projectx.item.data.Item.class.getDeclaredField(\"aLt\");\n" +
                                "        java.lang.reflect.Field aLuField = com.threerings.projectx.item.data.Item.class.getDeclaredField(\"aLu\");\n" +
                                "        aLsField.setAccessible(true);\n" +
                                "        aLtField.setAccessible(true);\n" +
                                "        aLuField.setAccessible(true);\n" +
                                "        java.lang.reflect.Field poEquipmentField = com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"equipment\");\n" +
                                "        java.lang.reflect.Field poItemsField = com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"items\");\n" +
                                "        poEquipmentField.setAccessible(true);\n" +
                                "        poItemsField.setAccessible(true);\n" +
                                "        long[] equipment = (long[]) poEquipmentField.get(playerObject);\n" +
                                "        com.threerings.presents.dobj.DSet items = (com.threerings.presents.dobj.DSet) poItemsField.get(playerObject);\n" +
                                "        for (int i = 0; i < equipment.length; i++) {\n" +
                                "            if (previewItems[i] != null) {\n" +
                                "                java.lang.Object item = items.f(Long.valueOf(equipment[i]));\n" +
                                "                if (item != null) {\n" +
                                "                    aLsField.set(previewItems[i], aLsField.get(item));\n" +
                                "                    aLtField.set(previewItems[i], aLtField.get(item));\n" +
                                "                }\n" +
                                "                previewItems[i].setDirty(true);\n" +
                                "            }\n" +
                                "        }\n" +
                                "\n" +
                                "        com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_equipmentPreview\").set(playerObject, previewItems);\n" +
                                "        com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_previewing\").set(playerObject, Boolean.valueOf(true));\n" +
                                "\n" +
                                "        System.out.println(\"Recreate model 1\");\n" +
                                "        com.threerings.crowd.client.d _d = this._ctx.rr();\n" +
                                "        java.lang.reflect.Field _FvField = com.threerings.crowd.client.d.class.getDeclaredField(\"Fv\");\n" +
                                "        _FvField.setAccessible(true);\n" +
                                "        java.lang.Object _FvObj = _FvField.get(_d);\n" +
                                "        if (_FvObj != null) {\n" +
                                "            System.out.println(\"Recreate model 2\");\n" +
                                "            com.threerings.crowd.client.t _t = (com.threerings.crowd.client.t) _FvObj;\n" +
                                "            if (_t instanceof com.threerings.projectx.client.eL) {\n" +
                                "                com.threerings.projectx.client.eL _el = (com.threerings.projectx.client.eL) _t;\n" +
                                "                java.lang.reflect.Method _hgMethod = com.threerings.projectx.client.eL.class.getDeclaredMethod(\"hg\", new java.lang.Class[0]);\n" +
                                "                _hgMethod.setAccessible(true);\n" +
                                "                _hgMethod.invoke(_el, new Object[0]);\n" +
                                "            } else {\n" +
                                "                com.threerings.projectx.client.aC _hud = com.threerings.projectx.client.aC.h(this._ctx);\n" +
                                "                com.threerings.tudey.a.b.a _actorSprite = _hud.vk().Eq();\n" +
                                "                com.threerings.projectx.client.sprite.KnightSprite _knightSprite = (com.threerings.projectx.client.sprite.KnightSprite) _actorSprite.OJ();\n" +
                                "                java.lang.reflect.Field _actorField = com.threerings.projectx.client.sprite.KnightSprite.class.getDeclaredField(\"_actor\");\n" +
                                "                java.lang.reflect.Field _configPreviewField = com.threerings.projectx.client.sprite.KnightSprite.class.getDeclaredField(\"_configPreview\");\n" +
                                "                _actorField.setAccessible(true);\n" +
                                "                _configPreviewField.setAccessible(true);\n" +
                                "                com.threerings.config.ConfigReference _previewConfigRef = com.threerings.projectx.data.PlayerObject.a(\"Character/PC/Default\", playerObject.a(this._ctx.getConfigManager(), true, true, -1), new Object[0]);\n" +
                                "                _configPreviewField.set(_knightSprite, _previewConfigRef);\n" +
                                "            }\n" +
                                "            System.out.println(\"Recreate model 3\");\n" +
                                "        }\n" +
                                "    } else {\n" +
                                "        System.out.println(\"Not Selected\");\n" +
                                "        com.threerings.projectx.data.PlayerObject playerObject = this._ctx.uk();\n" +
                                "        com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_equipmentPreview\").set(playerObject, null);\n" +
                                "        com.threerings.projectx.data.PlayerObject.class.getDeclaredField(\"_previewing\").set(playerObject, Boolean.valueOf(false));\n" +
                                "\n" +
                                "        com.threerings.projectx.client.aC _hud = com.threerings.projectx.client.aC.h(this._ctx);\n" +
                                "        com.threerings.tudey.a.b.a _actorSprite = _hud.vk().Eq();\n" +
                                "        com.threerings.projectx.client.sprite.KnightSprite _knightSprite = (com.threerings.projectx.client.sprite.KnightSprite) _actorSprite.OJ();\n" +
                                "        java.lang.reflect.Field _configPreviewField = com.threerings.projectx.client.sprite.KnightSprite.class.getDeclaredField(\"_configPreview\");\n" +
                                "        _configPreviewField.setAccessible(true);\n" +
                                "        _configPreviewField.set(_knightSprite, null);\n" +
                                "    }\n" +
                                "}"))
                .build();
    }

    static void redefineKnightSpriteToReplacePreviewItems() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.client.sprite.KnightSprite")
                .addField(new FieldBuilder()
                        .fieldName("_configPreview")
                        .typeName("com.threerings.config.ConfigReference")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT | Modifier.VOLATILE))
                .modifyMethod(new MethodModifier()
                        .methodName("c")
                        .paramTypeNames(new String[]{"com.threerings.tudey.data.actor.Actor"})
                        .insertBefore("" +
                                "if (this._configPreview != null) {\n" +
                                "    if (!this._configPreview.equals(this._actor.ES())) {\n" +
                                "        this._configPreview.copy(this._actor.ES());\n" +
                                "    }\n" +
                                "}"))
                .build();
    }

    static void redefineGoodSlotToIgnoreException() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.shop.client.GoodSlot")
                .modifyMethod(new MethodModifier()
                        .methodName("KC")
                        .body("{\n" +
                                "    boolean var1 = false;\n" +
                                "    boolean var2 = false;\n" +
                                "    com.threerings.projectx.util.A ctx = $0._ctx;\n" +
                                "    com.threerings.projectx.shop.config.GoodConfig aQs = $0.aQs;\n" +
                                "    com.threerings.projectx.shop.util.ItemPreparer aQu = $0.aQu;\n" +
                                "    if (aQs != null) {\n" +
                                "        try {\n" +
                                "            com.threerings.config.ConfigReference var3 = aQu.prepareItem(ctx, aQs);\n" +
                                "            com.threerings.projectx.item.config.ItemConfig.Original original = com.threerings.projectx.item.data.Item.j(ctx.getConfigManager(), var3);\n" +
                                "            com.threerings.projectx.item.data.Item var4 = original.b((com.threerings.projectx.item.b.a) ctx, var3);\n" +
                                "            var1 = var4.isLocked();\n" +
                                "            var2 = var4 instanceof com.threerings.projectx.item.data.LevelItem;\n" +
                                "        } catch (Throwable e) {\n" +
                                "        }\n" +
                                "    }\n" +
                                "    com.threerings.opengl.gui.q var5 = $0.getComponent(\"bound\");\n" +
                                "    var5.setEnabled(var1);\n" +
                                "    var5.setVisible(var1 || var2);\n" +
                                "}"))
                .build();
    }

    static void redefineVendorListPanelToIgnoreException() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.shop.client.u")
                .modifyMethod(new MethodModifier()
                        .methodName("j")
                        .body("{\n" +
                                "    java.lang.reflect.Field configMapField = com.threerings.projectx.shop.client.e.class.getDeclaredField(\"aQk\");\n" +
                                "    configMapField.setAccessible(true);\n" +
                                "    java.util.Map configMap = configMapField.get((com.threerings.projectx.shop.client.e) $0);\n" +
                                "    com.threerings.projectx.shop.data.UniqueShopInfo usi = $0.aQj.KD();\n" +
                                "    int i = 0;\n" +
                                "    int size = usi.goodCounts.size();\n" +
                                "    for (; i < size; ++i) {\n" +
                                "        com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount goodCount = (com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount) usi.goodCounts.get(i);\n" +
                                "        try {\n" +
                                "            com.threerings.config.ConfigReference itemConfigRef = goodCount.good.FX();\n" +
                                "            com.threerings.projectx.item.config.ItemConfig.Original itemConfigOriginal = com.threerings.projectx.item.data.Item.j($0._ctx.getConfigManager(), itemConfigRef);\n" +
                                "            com.threerings.projectx.client.d.c groupInfo = (com.threerings.projectx.client.d.c) $0.aEL.get(itemConfigOriginal.BA());\n" +
                                "            if (groupInfo != null && $0.bY(itemConfigOriginal.a((com.threerings.projectx.util.A) $0._ctx, itemConfigRef))) {\n" +
                                "                Object goodInfo = configMap.get(goodCount.good);\n" +
                                "                if (goodInfo == null) {\n" +
                                "                    goodInfo = new com.threerings.projectx.shop.client.e.a($0,goodCount.good, goodCount.count, i);\n" +
                                "                    configMap.put(goodCount.good, goodInfo);\n" +
                                "                }\n" +
                                "                groupInfo.abG.put(Integer.valueOf(i), goodInfo);\n" +
                                "            } else {\n" +
                                "                groupInfo.abG.remove(Integer.valueOf(i));\n" +
                                "            }\n" +
                                "        } catch (Exception e) {\n" +
                                "            System.out.println(\"[VendorListPanel] Failed to add item to pandora \" + goodCount.good.toString() + \"\\n\" + e.getMessage());\n" +
                                "        }\n" +
                                "    }\n" +
                                "}"))
                .build();
    }

    static void redefineShopDialogToCacheAndRenameTitle() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.shop.client.l")
                .modifyMethod(new MethodModifier()
                        .methodName("Bp")
                        .body("{\n" +
                                "    if (this.aQw.name == null || !this.aQw.name.toLowerCase().contains(\"pandora\")) {\n" +
                                "        this.atS = new com.threerings.projectx.shop.client.p(this, this._ctx, false);\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    if (this.atS != null) {\n" +
                                "        System.out.println(\"[Pandora] Use Cached VendorPanel\");\n" +
                                "        this._ctx.getRoot().addWindow(this.atS, true);\n" +
                                "        return;\n" +
                                "    }\n" +
                                "    System.out.println(\"[Pandora] Create New VendorPanel\");\n" +
                                "    this.atS = new com.threerings.projectx.shop.client.p(this, this._ctx, false);\n" +
                                "    com.threerings.projectx.shop.client.e abM = (com.threerings.projectx.shop.client.e) this.atS.tH();\n" +
                                "    ((com.threerings.opengl.gui.Label) abM.getComponent(\"title\")).setText(this.aQw.title);\n" +
                                "    com.threerings.opengl.gui.av btnPreview = ((com.threerings.opengl.gui.av) abM.getComponent(\"preview\"));\n" +
                                "    java.lang.reflect.Method registerComponentMethod = com.threerings.opengl.gui.ay.class.getDeclaredMethod(\"registerComponent\",\n" +
                                "            new Class[]{String.class, com.threerings.opengl.gui.q.class});\n" +
                                "    registerComponentMethod.setAccessible(true);\n" +
                                "    com.threerings.opengl.gui.av btnApply = new com.threerings.opengl.gui.av(this._ctx, \"Apply\");\n" +
                                "    btnApply.setStyleConfigs(btnPreview.getStyleConfigs());\n" +
                                "    btnApply.setSize(btnPreview.getWidth(), btnPreview.getHeight());\n" +
                                "    registerComponentMethod.invoke(abM, new Object[]{\"apply\", btnApply});\n" +
                                "    com.threerings.opengl.gui.event.a btnApplyListener =\n" +
                                "            (com.threerings.opengl.gui.event.a) Class.forName(\"com.spiralstudio.mod.pandora.PreviewApplyListener\")\n" +
                                "                    .getConstructors()[0].newInstance(new Object[]{this._ctx});\n" +
                                "    btnApply.addListener$2eebd3b8(btnApplyListener);\n" +
                                "    abM.add(btnApply, (Object) new Integer(1));\n" +
                                "    System.out.println(\"[Pandora] Add Apply Button\");\n" +
                                "}"))
                .build();
    }

    static void redefinePlayerObjectToPreview() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.data.PlayerObject")
                .addField(new FieldBuilder()
                        .fieldName("_equipmentPreview")
                        .typeName("com.threerings.projectx.item.data.LevelItem[]")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT))
                .addField(new FieldBuilder()
                        .fieldName("_previewing")
                        .typeName("boolean")
                        .modifiers(Modifier.PUBLIC | Modifier.TRANSIENT))
                .modifyMethod(new MethodModifier()
                        .methodName("cg")
                        .paramTypeNames(new String[]{"int"})
                        .body("{\n" +
                                "    if (this._previewing && this._equipmentPreview != null) {\n" +
                                "        com.threerings.projectx.item.data.LevelItem item = this._equipmentPreview[$1];\n" +
                                "        return item;\n" +
                                "    }\n" +
                                "    long id = this.equipment[$1];\n" +
                                "    if (id == 0L) {\n" +
                                "        return null;\n" +
                                "    }\n" +
                                "    com.threerings.projectx.item.data.Item item = (com.threerings.projectx.item.data.Item) this.items.f(Long.valueOf(id));\n" +
                                "    if (item instanceof com.threerings.projectx.item.data.LevelItem) {\n" +
                                "        return (com.threerings.projectx.item.data.LevelItem) item;\n" +
                                "    }\n" +
                                "    return null;\n" +
                                "}"))
                .build();
    }

    static void addPandoraCommand() {
        // Add a field for caching the window
        Commands.addField("_pandora", "com.threerings.opengl.gui.aE");
        // Add a command "/pandora"
        Commands.addCommand("pandora", "\n" +
                "        com.threerings.projectx.util.A ctxxx = (com.threerings.projectx.util.A) this._ctx;\n" +
                "        if (this._pandora != null) {\n" +
                "            ctxxx.getRoot().addWindow(this._pandora);\n" +
                "        } else {\n" +
                "            com.threerings.projectx.client.aC hud = com.threerings.projectx.client.aC.h(ctxxx);\n" +
                "            com.threerings.projectx.shop.data.ShopDialogInfo sdi = new com.threerings.projectx.shop.data.ShopDialogInfo();\n" +
                "            sdi.level = 0;\n" +
                "            sdi.type = com.threerings.projectx.shop.data.ShopDialogInfo.Type.CROWN;\n" +
                "            sdi.preparer = com.threerings.projectx.shop.util.ItemPreparer.BASIC;\n" +
                "            sdi.seedKnight = true;\n" +
                "            sdi.shopService = null;\n" +
                "            sdi.model = null;\n" +
                "            sdi.animation = null;\n" +
                "            sdi.name = \"Pandora\";\n" +
                "            sdi.title = \"Pandora\";\n" +
                "            sdi.sourceKey = new com.threerings.tudey.data.EntityKey.Actor(12);\n" +
                "            sdi.sourceTranslation = new com.threerings.math.Vector2f(11.377774F, 11.8713F);\n" +
                "            sdi.sourceRotation = -2.1118479F;\n" +
                "            sdi.sourceTransient = null;\n" +
                "            sdi.sourceCloseAnimation = null;\n" +
                "            com.threerings.projectx.shop.client.l shopDialog = new com.threerings.projectx.shop.client.l(ctxxx, hud.vk(), sdi);\n" +
                "            try {\n" +
                "                java.lang.reflect.Field cfgmgrField = com.threerings.opengl.e.class.getDeclaredField(\"_cfgmgr\");\n" +
                "                cfgmgrField.setAccessible(true);\n" +
                "                com.threerings.config.ConfigManager configManager = (com.threerings.config.ConfigManager) cfgmgrField.get(ctxxx);\n" +
                "\n" +
                "                java.lang.reflect.Field groupsField = com.threerings.config.ConfigManager.class.getDeclaredField(\"_groups\");\n" +
                "                groupsField.setAccessible(true);\n" +
                "                java.util.HashMap groups = (java.util.HashMap) groupsField.get(configManager);\n" +
                "                com.threerings.config.ConfigGroup group = (com.threerings.config.ConfigGroup) groups.get(com.threerings.projectx.item.config.ItemConfig.class);\n" +
                "\n" +
                "                java.lang.reflect.Field configsByNameField = com.threerings.config.ConfigGroup.class.getDeclaredField(\"_configsByName\");\n" +
                "                configsByNameField.setAccessible(true);\n" +
                "                java.util.HashMap configsByName = (java.util.HashMap) configsByNameField.get(group);\n" +
                "                java.lang.Iterable configs = configsByName.values();\n" +
                "                java.util.List goodCounts = new java.util.ArrayList();\n" +
                "                java.util.Iterator iterator = configs.iterator();\n" +
                "                while (iterator.hasNext()) {\n" +
                "                    com.threerings.projectx.item.config.ItemConfig o = (com.threerings.projectx.item.config.ItemConfig) iterator.next();\n" +
                "                    if (o.getName().startsWith(\"Weapon/PvP/\")) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    com.threerings.config.ConfigReference ref = o.getReference();\n" +
                "                    goodCounts.add(new com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount(\n" +
                "                            new com.threerings.projectx.shop.config.GoodConfig.Item(ref),\n" +
                "                            -1));\n" +
                "                }\n" +
                "                com.threerings.projectx.shop.data.UniqueShopInfo usi = new com.threerings.projectx.shop.data.UniqueShopInfo();\n" +
                "                java.lang.reflect.Field goodCountsField = com.threerings.projectx.shop.data.UniqueShopInfo.class.getDeclaredField(\"goodCounts\");\n" +
                "                goodCountsField.setAccessible(true);\n" +
                "                goodCountsField.set(usi, goodCounts);\n" +
                "                java.lang.reflect.Field aQxField = com.threerings.projectx.shop.client.l.class.getDeclaredField(\"aQx\");\n" +
                "                aQxField.setAccessible(true);\n" +
                "                aQxField.set(shopDialog, usi);\n" +
                "            } catch (Exception e) {\n" +
                "                throw new RuntimeException(e);\n" +
                "            }\n" +
                "            this._pandora = shopDialog;\n" +
                "            ctxxx.getRoot().addWindow(shopDialog);\n" +
                "        }");
        // Add a command "/clearpandora" to help GC
        Commands.addCommand("clearpandora", "this._pandora = null;");
    }

    public static void main(String[] args) {
    }
}
