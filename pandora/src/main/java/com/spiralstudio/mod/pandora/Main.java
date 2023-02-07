package com.spiralstudio.mod.pandora;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.item.client.ArsenalPanel
 * @see com.threerings.projectx.shop.client.l ShopDialog
 * @see com.threerings.projectx.shop.data.ShopDialogInfo
 * @see com.threerings.projectx.shop.data.UniqueShopInfo
 */
public class Main {
    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            redefineGoodSlotToIgnoreException(classPool);
            redefineVendorListPanelToIgnoreException(classPool);
            redefineShopDialogToRenameTitle(classPool);
            redefineHudWindowToReplaceHelpWindow(classPool);
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    static void redefineGoodSlotToIgnoreException(ClassPool classPool) throws Exception {
        CtClass ctClass = classPool.get("com.threerings.projectx.shop.client.GoodSlot");
        CtMethod ctMethod = ctClass.getDeclaredMethod("KC");
        ctMethod.setBody("{\n" +
                "            boolean var1 = false;\n" +
                "            boolean var2 = false;\n" +
                "            com.threerings.projectx.util.A ctx = $0._ctx;\n" +
                "            com.threerings.projectx.shop.config.GoodConfig aQs = $0.aQs;\n" +
                "            com.threerings.projectx.shop.util.ItemPreparer aQu = $0.aQu;\n" +
                "            if (aQs != null) {\n" +
                "                try {\n" +
                "                    com.threerings.config.ConfigReference var3 = aQu.prepareItem(ctx, aQs);\n" +
                "                    com.threerings.projectx.item.config.ItemConfig.Original original = com.threerings.projectx.item.data.Item.j(ctx.getConfigManager(), var3);\n" +
                "                    com.threerings.projectx.item.data.Item var4 = original.b((com.threerings.projectx.item.b.a) ctx, var3);\n" +
                "                    var1 = var4.isLocked();\n" +
                "                    var2 = var4 instanceof com.threerings.projectx.item.data.LevelItem;\n" +
                "                } catch (Throwable e) {\n" +
                "                }\n" +
                "            }\n" +
                "            com.threerings.opengl.gui.q var5 = $0.getComponent(\"bound\");\n" +
                "            var5.setEnabled(var1);\n" +
                "            var5.setVisible(var1 || var2);\n" +
                "        }");
        ctClass.toClass();
        ctClass.detach();

        /*{
            boolean var1 = false;
            boolean var2 = false;
            com.threerings.projectx.util.A ctx = $0._ctx;
            com.threerings.projectx.shop.config.GoodConfig aQs = $0.aQs;
            com.threerings.projectx.shop.util.ItemPreparer aQu = $0.aQu;
            if (aQs != null) {
                try {
                    com.threerings.config.ConfigReference var3 = aQu.prepareItem(ctx, aQs);
                    com.threerings.projectx.item.config.ItemConfig.Original original = com.threerings.projectx.item.data.Item.j(ctx.getConfigManager(), var3);
                    com.threerings.projectx.item.data.Item var4 = original.b((com.threerings.projectx.item.b.a) ctx, var3);
                    var1 = var4.isLocked();
                    var2 = var4 instanceof com.threerings.projectx.item.data.LevelItem;
                } catch (Throwable e) {
                    System.out.println(e.getMessage());
                }
            }
            com.threerings.opengl.gui.q var5 = $0.getComponent("bound");
            var5.setEnabled(var1);
            var5.setVisible(var1 || var2);
        }*/
    }

    static void redefineVendorListPanelToIgnoreException(ClassPool classPool) throws Exception {
        CtClass ctClass = classPool.get("com.threerings.projectx.shop.client.u");
        CtMethod ctMethod = ctClass.getDeclaredMethod("j");
        ctMethod.setBody("{\n" +
                "            com.threerings.projectx.shop.data.UniqueShopInfo var8 = $0.aQj.KD();\n" +
                "            int var2 = 0;\n" +
                "            for (int var3 = var8.goodCounts.size(); var2 < var3; ++var2) {\n" +
                "                com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount var4 = (com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount) var8.goodCounts.get(var2);\n" +
                "                try {\n" +
                "                    if (var4 == null) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    com.threerings.config.ConfigReference var5 = var4.good.FX();\n" +
                "                    if (var5 == null) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    com.threerings.projectx.item.config.ItemConfig.Original var6 = com.threerings.projectx.item.data.Item.j($0._ctx.getConfigManager(), var5);\n" +
                "                    if (var6 == null) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    com.threerings.projectx.client.d.c var7 = (com.threerings.projectx.client.d.c) $0.aEL.get(var6.BA());\n" +
                "                    if (var7 == null) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    if (var7.abG == null) {\n" +
                "                        continue;\n" +
                "                    }\n" +
                "                    if (var7 != null && $0.bY(var6.a($0._ctx, var5)) && var4.count != 0) {\n" +
                "                        var7.abG.put(Integer.valueOf(var2), (Object) $0.a(var4.good, var4.count, var2));\n" +
                "                    } else {\n" +
                "                        $0.a((com.threerings.projectx.shop.client.e.a) var7.abG.remove(Integer.valueOf(var2)));\n" +
                "                    }\n" +
                "                } catch (Exception e) {\n" +
                "                }\n" +
                "            }\n" +
                "        }");
        ctClass.toClass();
        ctClass.detach();

        /*{
            com.threerings.projectx.shop.data.UniqueShopInfo var8 = $0.aQj.KD();
            int var2 = 0;
            for (int var3 = var8.goodCounts.size(); var2 < var3; ++var2) {
                com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount var4 = (com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount) var8.goodCounts.get(var2);
                try {
                    if (var4 == null) {
                        System.out.println("UniqueShopInfo.GoodCount is null: " + var2);
                        continue;
                    }
                    com.threerings.config.ConfigReference var5 = var4.good.FX();
                    if (var5 == null) {
                        System.out.println("ConfigReference is null: " + var4.toString());
                        continue;
                    }
                    com.threerings.projectx.item.config.ItemConfig.Original var6 = com.threerings.projectx.item.data.Item.j($0._ctx.getConfigManager(), var5);
                    if (var6 == null) {
                        System.out.println("ItemConfig.Original is null: " + var5.toString());
                        continue;
                    }
                    com.threerings.projectx.client.d.c var7 = (com.threerings.projectx.client.d.c) $0.aEL.get(var6.BA());
                    if (var7 == null) {
                        System.out.println("GroupInfo is null: " + var6.toString());
                        continue;
                    }
                    if (var7.abG == null) {
                        System.out.println("infos is null: " + var6.toString());
                        continue;
                    }
                    if (var7 != null && $0.bY(var6.a($0._ctx, var5)) && var4.count != 0) {
                        var7.abG.put(Integer.valueOf(var2), (Object) $0.a(var4.good, var4.count, var2));
                    } else {
                        $0.a((com.threerings.projectx.shop.client.e.a) var7.abG.remove(Integer.valueOf(var2)));
                    }
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage() + "\n" + var4.good.toString());
                }
            }
        }*/
    }

    static void redefineShopDialogToRenameTitle(ClassPool classPool) throws Exception {
        CtClass ctClass2 = classPool.get("com.threerings.projectx.shop.client.l");
        CtMethod ctMethod2 = ctClass2.getDeclaredMethod("Bp");
        ctMethod2.setBody("{\n" +
                "            com.threerings.projectx.client.g atS = new com.threerings.projectx.shop.client.p($0, $0._ctx, false);\n" +
                "            com.threerings.projectx.shop.client.e abM = ( com.threerings.projectx.shop.client.e) atS.tH();\n" +
                "            ((com.threerings.opengl.gui.Label)abM.getComponent(\"title\")).setText(\"Pandora\");\n" +
                "            return atS;\n" +
                "        }");
        ctClass2.toClass();
        ctClass2.detach();
        /*{
            com.threerings.projectx.client.g atS = new com.threerings.projectx.shop.client.p($0, $0._ctx, false);
            com.threerings.projectx.shop.client.e abM = ( com.threerings.projectx.shop.client.e) atS.tH();
            ((com.threerings.opengl.gui.Label)abM.getComponent("title")).setText("Pandora");
            return atS;
        }*/
    }

    static void redefineHudWindowToReplaceHelpWindow(ClassPool classPool) throws Exception {
        CtClass ctClass = classPool.get("com.threerings.projectx.client.aC");

        CtField ctField = new CtField(classPool.get("com.threerings.opengl.gui.aE"), "_pandora", ctClass);
        ctField.setModifiers(Modifier.PUBLIC);
        ctClass.addField(ctField);

        CtMethod ctMethod = ctClass.getDeclaredMethod("vc");
        ctMethod.setBody("{\n" +
                "            com.threerings.opengl.gui.aE pb = $0._pandora;\n" +
                "            if (pb != null ) {\n" +
                "                if (pb.isAdded()) {\n" +
                "                    if (pb instanceof com.threerings.projectx.client.ui.SlidingPanel) {\n" +
                "                        com.threerings.projectx.client.ui.SlidingPanel var6 = (com.threerings.projectx.client.ui.SlidingPanel) pb;\n" +
                "                        if (!var6.Av()) {\n" +
                "                            var6.Au();\n" +
                "                        }\n" +
                "                    } else {\n" +
                "                        pb.dismiss();\n" +
                "                    }\n" +
                "                } else {\n" +
                "                    $0._ctx.getRoot().addWindow(pb, true);\n" +
                "                }\n" +
                "                return;\n" +
                "            }\n" +
                "            System.out.println($0.acf);\n" +
                "            System.out.println($0.acf.uk());\n" +
                "            com.threerings.projectx.shop.data.ShopDialogInfo sdi = new com.threerings.projectx.shop.data.ShopDialogInfo();\n" +
                "            sdi.level = 0;\n" +
                "            sdi.type = com.threerings.projectx.shop.data.ShopDialogInfo.Type.CROWN;\n" +
                "            sdi.preparer = com.threerings.projectx.shop.util.ItemPreparer.BASIC;\n" +
                "            sdi.seedKnight = true;\n" +
                "            sdi.shopService = null;\n" +
                "            sdi.model = null;\n" +
                "            sdi.animation = null;\n" +
                "            sdi.name = \"m.haven_weapon_1n\";\n" +
                "            sdi.title = \"m.haven_weapon_1t\";\n" +
                "            sdi.sourceKey = new com.threerings.tudey.data.EntityKey.Actor(12);\n" +
                "            sdi.sourceTranslation = new com.threerings.math.Vector2f(11.377774F, 11.8713F);\n" +
                "            sdi.sourceRotation = -2.1118479F;\n" +
                "            sdi.sourceTransient = null;\n" +
                "            sdi.sourceCloseAnimation = null;\n" +
                "            com.threerings.projectx.shop.client.l shopDialog = new com.threerings.projectx.shop.client.l($0._ctx, $0.acf, sdi);\n" +
                "            try {\n" +
                "                java.lang.reflect.Field cfgmgrField = com.threerings.opengl.e.class.getDeclaredField(\"_cfgmgr\");\n" +
                "                cfgmgrField.setAccessible(true);\n" +
                "                com.threerings.config.ConfigManager configManager = (com.threerings.config.ConfigManager) cfgmgrField.get($0._ctx);\n" +
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
                "                    com.threerings.config.ManagedConfig o = (com.threerings.config.ManagedConfig) iterator.next();\n" +
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
                "            com.threerings.opengl.gui.aE a = shopDialog;\n" +
                "            $0._pandora = a;\n" +
                "            $0._ctx.getRoot().addWindow(a, true);\n" +
                "        }");
        ctClass.toClass();
        ctClass.detach();

        /*{
            com.threerings.opengl.gui.aE pb = $0._pandora;
            if (pb != null ) {
                if (pb.isAdded()) {
                    if (pb instanceof com.threerings.projectx.client.ui.SlidingPanel) {
                        com.threerings.projectx.client.ui.SlidingPanel var6 = (com.threerings.projectx.client.ui.SlidingPanel) pb;
                        if (!var6.Av()) {
                            var6.Au();
                        }
                    } else {
                        pb.dismiss();
                    }
                } else {
                    $0._ctx.getRoot().addWindow(pb, true);
                }
                return;
            }
            System.out.println($0.acf);
            System.out.println($0.acf.uk());
            com.threerings.projectx.shop.data.ShopDialogInfo sdi = new com.threerings.projectx.shop.data.ShopDialogInfo();
            sdi.level = 0;
            sdi.type = com.threerings.projectx.shop.data.ShopDialogInfo.Type.CROWN;
            sdi.preparer = com.threerings.projectx.shop.util.ItemPreparer.BASIC;
            sdi.seedKnight = true;
            sdi.shopService = null;
            sdi.model = null;
            sdi.animation = null;
            sdi.name = "m.haven_weapon_1n";
            sdi.title = "m.haven_weapon_1t";
            sdi.sourceKey = new com.threerings.tudey.data.EntityKey.Actor(12);
            sdi.sourceTranslation = new com.threerings.math.Vector2f(11.377774F, 11.8713F);
            sdi.sourceRotation = -2.1118479F;
            sdi.sourceTransient = null;
            sdi.sourceCloseAnimation = null;
            com.threerings.projectx.shop.client.l shopDialog = new com.threerings.projectx.shop.client.l($0._ctx, $0.acf, sdi);
            try {
                java.lang.reflect.Field cfgmgrField = com.threerings.opengl.e.class.getDeclaredField("_cfgmgr");
                cfgmgrField.setAccessible(true);
                com.threerings.config.ConfigManager configManager = (com.threerings.config.ConfigManager) cfgmgrField.get($0._ctx);

                java.lang.reflect.Field groupsField = com.threerings.config.ConfigManager.class.getDeclaredField("_groups");
                groupsField.setAccessible(true);
                java.util.HashMap groups = (java.util.HashMap) groupsField.get(configManager);
                com.threerings.config.ConfigGroup group = (com.threerings.config.ConfigGroup) groups.get(com.threerings.projectx.item.config.ItemConfig.class);

                java.lang.reflect.Field configsByNameField = com.threerings.config.ConfigGroup.class.getDeclaredField("_configsByName");
                configsByNameField.setAccessible(true);
                java.util.HashMap configsByName = (java.util.HashMap) configsByNameField.get(group);
                java.lang.Iterable configs = configsByName.values();
                java.util.List goodCounts = new java.util.ArrayList();
                java.util.Iterator iterator = configs.iterator();
                while (iterator.hasNext()) {
                    com.threerings.config.ManagedConfig o = (com.threerings.config.ManagedConfig) iterator.next();
                    com.threerings.config.ConfigReference ref = o.getReference();
                    goodCounts.add(new com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount(
                            new com.threerings.projectx.shop.config.GoodConfig.Item(ref),
                            -1));
                }
                com.threerings.projectx.shop.data.UniqueShopInfo usi = new com.threerings.projectx.shop.data.UniqueShopInfo();
                java.lang.reflect.Field goodCountsField = com.threerings.projectx.shop.data.UniqueShopInfo.class.getDeclaredField("goodCounts");
                goodCountsField.setAccessible(true);
                goodCountsField.set(usi, goodCounts);
                java.lang.reflect.Field aQxField = com.threerings.projectx.shop.client.l.class.getDeclaredField("aQx");
                aQxField.setAccessible(true);
                aQxField.set(shopDialog, usi);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            com.threerings.opengl.gui.aE a = shopDialog;
            $0._pandora = a;
            $0._ctx.getRoot().addWindow(a, true);
        }*/
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
