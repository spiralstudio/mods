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
                "            java.lang.reflect.Field configMapField = com.threerings.projectx.shop.client.e.class.getDeclaredField(\"aQk\");\n" +
                "            configMapField.setAccessible(true);\n" +
                "            java.util.Map configMap = configMapField.get((com.threerings.projectx.shop.client.e) $0);\n" +
                "            com.threerings.projectx.shop.data.UniqueShopInfo usi = $0.aQj.KD();\n" +
                "            int i = 0;\n" +
                "            int size = usi.goodCounts.size();\n" +
                "            for (; i < size; ++i) {\n" +
                "                com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount goodCount = (com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount) usi.goodCounts.get(i);\n" +
                "                try {\n" +
                "                    com.threerings.config.ConfigReference itemConfigRef = goodCount.good.FX();\n" +
                "                    com.threerings.projectx.item.config.ItemConfig.Original itemConfigOriginal = com.threerings.projectx.item.data.Item.j($0._ctx.getConfigManager(), itemConfigRef);\n" +
                "                    com.threerings.projectx.client.d.c groupInfo = (com.threerings.projectx.client.d.c) $0.aEL.get(itemConfigOriginal.BA());\n" +
                "                    if (groupInfo != null && $0.bY(itemConfigOriginal.a((com.threerings.projectx.util.A) $0._ctx, itemConfigRef))) {\n" +
                "                        Object goodInfo = configMap.get(goodCount.good);\n" +
                "                        if (goodInfo == null) {\n" +
                "                            goodInfo = new com.threerings.projectx.shop.client.e.a($0,goodCount.good, goodCount.count, i);\n" +
                "                            configMap.put(goodCount.good, goodInfo);\n" +
                "                        }\n" +
                "                        groupInfo.abG.put(Integer.valueOf(i), goodInfo);\n" +
                "                    } else {\n" +
                "                        groupInfo.abG.remove(Integer.valueOf(i));\n" +
                "                    }\n" +
                "                } catch (Exception e) {\n" +
                "                    System.out.println(\"[VendorListPanel] Failed to add item to pandora \" + goodCount.good.toString() + \"\\n\" + e.getMessage());\n" +
                "                }\n" +
                "            }\n" +
                "        }");
        ctClass.toClass();
        ctClass.detach();

        /*{
            java.lang.reflect.Field configMapField = com.threerings.projectx.shop.client.e.class.getDeclaredField("aQk");
            configMapField.setAccessible(true);
            java.util.Map configMap = configMapField.get((com.threerings.projectx.shop.client.e) $0);
            com.threerings.projectx.shop.data.UniqueShopInfo usi = $0.aQj.KD();
            int i = 0;
            int size = usi.goodCounts.size();
            for (; i < size; ++i) {
                com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount goodCount = (com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount) usi.goodCounts.get(i);
                try {
                    com.threerings.config.ConfigReference itemConfigRef = goodCount.good.FX();
                    com.threerings.projectx.item.config.ItemConfig.Original itemConfigOriginal = com.threerings.projectx.item.data.Item.j($0._ctx.getConfigManager(), itemConfigRef);
                    com.threerings.projectx.client.d.c groupInfo = (com.threerings.projectx.client.d.c) $0.aEL.get(itemConfigOriginal.BA());
                    if (groupInfo != null && $0.bY(itemConfigOriginal.a((com.threerings.projectx.util.A) $0._ctx, itemConfigRef))) {
                        Object goodInfo = configMap.get(goodCount.good);
                        if (goodInfo == null) {
                            goodInfo = new com.threerings.projectx.shop.client.e.a($0,goodCount.good, goodCount.count, i);
                            configMap.put(goodCount.good, goodInfo);
                        }
                        groupInfo.abG.put(Integer.valueOf(i), goodInfo);
                    } else {
                        groupInfo.abG.remove(Integer.valueOf(i));
                    }
                } catch (Exception e) {
                    System.out.println("[VendorListPanel] Failed to add item to pandora " + goodCount.good.toString() + "\n" + e.getMessage());
                }
            }
        }*/

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
                "            if (this.atS != null) {\n" +
                "                System.out.println(\"Use Cached VendorPanel\");\n" +
                "                $0._ctx.getRoot().addWindow(this.atS, true);\n" +
                "                return this.atS;\n" +
                "            }\n" +
                "            System.out.println(\"Create VendorPanel\");\n" +
                "            this.atS = new com.threerings.projectx.shop.client.p($0, $0._ctx, false);\n" +
                "            if ($0.aQw.name != null && $0.aQw.name.toLowerCase().contains(\"pandora\")) {\n" +
                "                com.threerings.projectx.shop.client.e abM = (com.threerings.projectx.shop.client.e) this.atS.tH();\n" +
                "                ((com.threerings.opengl.gui.Label) abM.getComponent(\"title\")).setText($0.aQw.title);\n" +
                "            }\n" +
                "            return this.atS;\n" +
                "        }");
        ctClass2.toClass();
        ctClass2.detach();

        /*{
            if (this.atS != null) {
                System.out.println("Use Cached VendorPanel");
                $0._ctx.getRoot().addWindow(this.atS, true);
                return this.atS;
            }
            System.out.println("Create VendorPanel");
            this.atS = new com.threerings.projectx.shop.client.p($0, $0._ctx, false);
            if ($0.aQw.name != null && $0.aQw.name.toLowerCase().contains("pandora")) {
                com.threerings.projectx.shop.client.e abM = (com.threerings.projectx.shop.client.e) this.atS.tH();
                ((com.threerings.opengl.gui.Label) abM.getComponent("title")).setText($0.aQw.title);
            }
            return this.atS;
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
                "                    $0._ctx.getRoot().addWindow(pb);\n" +
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
                "            sdi.name = \"Pandora\";\n" +
                "            sdi.title = \"Pandora's Box\";\n" +
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
                "            $0._ctx.getRoot().addWindow(a);\n" +
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
                    $0._ctx.getRoot().addWindow(pb);
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
            sdi.name = "Pandora";
            sdi.title = "Pandora's Box";
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
            $0._ctx.getRoot().addWindow(a);
        }*/
    }

    static void redefineMessageBundleToIgnoreException(ClassPool classPool) throws Exception {
        CtClass ctClass = classPool.get("com.threerings.util.N");
        CtMethod ctMethod = ctClass.getDeclaredMethod("m", classPool.get(new String[]{"java.lang.String", "boolean"}));
        ctMethod.setBody("{\n" +
                "            try {\n" +
                "                if (this.beF != null) {\n" +
                "                    return this.beF.getString($1);\n" +
                "                }\n" +
                "            } catch (java.lang.Exception var4) {\n" +
                "            }\n" +
                "            String var3;\n" +
                "            if (this.beG != null && (var3 = this.beG.m($1, false)) != null) {\n" +
                "                return var3;\n" +
                "            } else {\n" +
                "                return null;\n" +
                "            }\n" +
                "        }");

        /*{
            try {
                if (this.beF != null) {
                    return this.beF.getString($1);
                }
            } catch (java.lang.Exception var4) {
            }
            String var3;
            if (this.beG != null && (var3 = this.beG.m($1, false)) != null) {
                return var3;
            } else {
                return null;
            }
        }*/

        CtMethod ctMethod2 = ctClass.getDeclaredMethod("e");
        ctMethod2.setBody("{\n" +
                "            com.threerings.util.N self = this;\n" +
                "            while($1.startsWith(\"%\")) {\n" +
                "                com.threerings.util.N nnnnn = self._msgmgr.dI(com.threerings.util.N.dG($1));\n" +
                "                $1 = com.threerings.util.N.dH($1);\n" +
                "                self = nnnnn;\n" +
                "            }\n" +
                "\n" +
                "            java.lang.String var3 = com.threerings.util.N.l($2);\n" +
                "            java.lang.String var4;\n" +
                "            if ((var4 = self.m($1 + var3, false)) == null) {\n" +
                "                if (!var3.equals(\"\")) {\n" +
                "                    var4 = self.m($1, false);\n" +
                "                }\n" +
                "\n" +
                "                if (var4 == null) {\n" +
                "                    return $1 + com.samskivert.util.aq.toString($2);\n" +
                "                }\n" +
                "            }\n" +
                "\n" +
                "            try {\n" +
                "                return java.text.MessageFormat.format(var4.replace(\"'\", \"''\"), $2);\n" +
                "            } catch (IllegalArgumentException var5) {\n" +
                "                return var4 + com.samskivert.util.aq.toString($2);\n" +
                "            }\n" +
                "        }");


        /*{
            com.threerings.util.N self = this;
            while($1.startsWith("%")) {
                com.threerings.util.N nnnnn = self._msgmgr.dI(com.threerings.util.N.dG($1));
                $1 = com.threerings.util.N.dH($1);
                self = nnnnn;
            }

            java.lang.String var3 = com.threerings.util.N.l($2);
            java.lang.String var4;
            if ((var4 = self.m($1 + var3, false)) == null) {
                if (!var3.equals("")) {
                    var4 = self.m($1, false);
                }

                if (var4 == null) {
                    return $1 + com.samskivert.util.aq.toString($2);
                }
            }

            try {
                return java.text.MessageFormat.format(var4.replace("'", "''"), $2);
            } catch (IllegalArgumentException var5) {
                return var4 + com.samskivert.util.aq.toString($2);
            }
        }*/

        ctClass.toClass();
        ctClass.detach();
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
