package com.spiralstudio.mod.gearex;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.item.config.ItemConfig
 */
public class Main {
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineItemConfigGear();
        redefineItemConfigWeapon();
        redefineItemConfigShield();
    }

    static void redefineItemConfigGear() {
        // TODO
    }

    static void redefineItemConfigWeapon() {
        ClassPool.from("com.threerings.projectx.item.config.ItemConfig$Weapon")
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .paramTypeNames("com.threerings.projectx.util.A", "com.threerings.projectx.item.data.LevelItem", "com.threerings.util.N", "java.util.List", "java.util.Map")
                        .body("" +
                                "{\n" +
                                "    com.threerings.projectx.util.A var1 = $1;\n" +
                                "    com.threerings.projectx.item.data.LevelItem var2 = $2;\n" +
                                "    com.threerings.util.N var3 = $3;\n" +
                                "    java.util.List var4 = $4;\n" +
                                "    java.util.Map var5 = $5;\n" +
                                "    com.threerings.opengl.gui.ay var6 = new com.threerings.opengl.gui.ay(var1, \"ui/window/tooltip_parts/container_stats.dat\", \"Stat Title\", var3.get(\"t.attack_power\"), new java.lang.Object[0]);\n" +
                                "    com.threerings.opengl.gui.Container var7 = (com.threerings.opengl.gui.Container) (var6).getComponent(\"stats\");\n" +
                                "    var7.remove(1);\n" +
                                "    com.threerings.config.ConfigManager var8 = var1.getConfigManager();\n" +
                                "    com.threerings.projectx.dungeon.util.DamageSummary var9 = new com.threerings.projectx.dungeon.util.DamageSummary();\n" +
                                "    this.a(var1, var9);\n" +
                                "    com.threerings.projectx.dungeon.util.DamageSummary var10 = var9.Gr();\n" +
                                "    com.threerings.projectx.item.data.EquipmentModifiers var11 = new com.threerings.projectx.item.data.EquipmentModifiers();\n" +
                                "    com.threerings.projectx.item.data.EquipmentModifiers var12 = new com.threerings.projectx.item.data.EquipmentModifiers();\n" +
                                "    com.threerings.projectx.item.config.LevelConfig var13 = var2.d(var8, -1);\n" +
                                "    var13.a(var9, 30, (com.threerings.projectx.item.config.LevelConfig.Weapons) null, false);\n" +
                                "    var13.a(var11, this);\n" +
                                "    var13 = var2.d(var8, this.M(var8) - 1);\n" +
                                "    var13.a(var10, 30, (com.threerings.projectx.item.config.LevelConfig.Weapons) null, false);\n" +
                                "    var13.a(var12, this);\n" +
                                "    var2.a(var8, var11);\n" +
                                "    var2.a(var8, var12);\n" +
                                "    com.threerings.projectx.dungeon.util.DamageSummary.a var20 = var9.Gp();\n" +
                                "    com.threerings.projectx.dungeon.util.DamageSummary.a var21 = var10.Gp();\n" +
                                "    com.threerings.projectx.dungeon.util.DamageSummary.DamageType[] var22 = com.threerings.projectx.item.data.ItemCodes.aLP;\n" +
                                "    int var24 = var22.length;\n" +
                                "\n" +
                                "    com.threerings.config.ConfigReference _styleConfigRef = new com.threerings.config.ConfigReference(\"Currency/Energy\");\n" +
                                "    com.threerings.opengl.gui.config.StyleConfig _styleConfig = (com.threerings.opengl.gui.config.StyleConfig) var1.getConfigManager().a(com.threerings.opengl.gui.config.StyleConfig.class, (com.threerings.config.ConfigReference) _styleConfigRef.clone());\n" +
                                "    com.threerings.opengl.gui.config.StyleConfig.Original _styleConfigOriginal = _styleConfig.getOriginal$45fbbce3(var1);\n" +
                                "    _styleConfigOriginal.color = new com.threerings.opengl.renderer.Color4f(179F / 255F, 238F / 255F, 58F / 255F, 1F);\n" +
                                "\n" +
                                "    int var25;\n" +
                                "    for (var25 = 0; var25 < var24; ++var25) {\n" +
                                "        com.threerings.projectx.dungeon.util.DamageSummary.DamageType var14 = var22[var25];\n" +
                                "        float var15 = ((com.threerings.expr.ag) var20.aEa.bc(var14)).value * 0.41666666F;\n" +
                                "        float var16 = ((com.threerings.expr.ag) var21.aEa.bc(var14)).value * 0.41666666F;\n" +
                                "        boolean var17 = var15 > 0.0F || var16 > 0.0F;\n" +
                                "        if (com.threerings.projectx.item.config.ItemConfig.Level.a(var5, (java.lang.Object) var14, var17)) {\n" +
                                "            java.lang.String var18 = var14.toString().toLowerCase();\n" +
                                "            int var27 = Math.min((int) (var15 > 0.0F ? var15 + 14.0F : 0.0F), 164);\n" +
                                "            int var29 = Math.min((int) (var16 > 0.0F ? var16 + 14.0F : 0.0F), 164);\n" +
                                "            java.lang.String var19 = this.a(var5, var14, var27);\n" +
                                "            com.threerings.opengl.gui.ay var30 = new com.threerings.opengl.gui.ay(var1, \"ui/window/tooltip_parts/stat_bar.dat\", \"Icon\", \"ui/icon/stats/attack_\" + var18 + \".png\", new java.lang.Object[]{\"Stat Label\", var3.get(\"t.\" + var18), \"Alignment\", \"Left\", \"Bonus Width\", Integer.valueOf(var27), \"Bonus Potential Width\", Integer.valueOf(var29), \"Bar Type\", var19});\n" +
                                "            com.threerings.opengl.gui.Label _statLabel = ((com.threerings.opengl.gui.Label) var30.getComponent(\"stat_label\"));\n" +
                                "            if (var15 == var16) {\n" +
                                "                _statLabel.setText(_statLabel.getText() + \" \" + Integer.toString((int) var15));\n" +
                                "            } else {\n" +
                                "                _statLabel.setText(_statLabel.getText() + \" \" + Integer.toString((int) var15) + \"/\" + Integer.toString((int) var16));\n" +
                                "            }\n" +
                                "            _statLabel.setStyleConfigs(new com.threerings.opengl.gui.config.StyleConfig[]{_styleConfig});\n" +
                                "            var7.add(var30);\n" +
                                "            if (var5 != null) {\n" +
                                "                var5.put(var14, new com.threerings.projectx.item.config.ItemConfig.a(var30, var27, var17));\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }\n" +
                                "    float var23 = 1.0F + com.threerings.projectx.item.data.ItemCodes.ItemValueKey.SPEED.capped(var11.attackSpeed);\n" +
                                "    var24 = Math.min((int) ((float) this.displaySpeed * var23 * 15.0F) + 14, 164);\n" +
                                "    var23 = 1.0F + com.threerings.projectx.item.data.ItemCodes.ItemValueKey.SPEED.capped(var12.attackSpeed);\n" +
                                "    var25 = Math.min((int) ((float) this.displaySpeed * var23 * 15.0F) + 14, 164);\n" +
                                "    java.lang.String var26 = this.a(var5, \"speed\", var24);\n" +
                                "    com.threerings.opengl.gui.ay var28 = new com.threerings.opengl.gui.ay(var1, \"ui/window/tooltip_parts/stat_bar.dat\", \"Icon\", \"ui/icon/stats/attack_speed.png\", new java.lang.Object[]{\"Stat Label\", var3.get(\"t.speed\"), \"Alignment\", \"Left\", \"Bonus Width\", Integer.valueOf(var24), \"Bonus Potential Width\", Integer.valueOf(var25), \"Bar Type\", var26});\n" +
                                "    com.threerings.opengl.gui.Label _statLabel = ((com.threerings.opengl.gui.Label) var28.getComponent(\"stat_label\"));\n" +
                                "    if (var11.attackSpeed == var12.attackSpeed) {\n" +
                                "        _statLabel.setText(_statLabel.getText() + \" \" + Integer.toString((int) var24));\n" +
                                "    } else {\n" +
                                "        _statLabel.setText(_statLabel.getText() + \" \" + Integer.toString((int) var24) + \"/\" + Integer.toString((int) var25));\n" +
                                "    }\n" +
                                "    _statLabel.setStyleConfigs(new com.threerings.opengl.gui.config.StyleConfig[]{_styleConfig});\n" +
                                "    var7.add(var28);\n" +
                                "    if (var5 != null) {\n" +
                                "        var5.put(\"speed\", new com.threerings.projectx.item.config.ItemConfig.a(var28, var24, true));\n" +
                                "    }\n" +
                                "    if (var7.getComponentCount() > 1) {\n" +
                                "        var4.add(var6);\n" +
                                "    }\n" +
                                "    this.a(var1, var3, var4);\n" +
                                "}"));
    }

    static void redefineItemConfigShield() {
        ClassPool.from("com.threerings.projectx.item.config.ItemConfig$Shield")
                .modifyMethod(new MethodModifier()
                        .methodName("a")
                        .paramTypeNames("com.threerings.projectx.util.A", "com.threerings.util.N", "com.threerings.opengl.gui.Container", "java.util.Map")
                        .body("{\n" +
                                "    com.threerings.projectx.util.A var1 = $1;\n" +
                                "    com.threerings.util.N var2 = $2;\n" +
                                "    com.threerings.opengl.gui.Container var3 = $3;\n" +
                                "    java.util.Map var4 = $4;\n" +
                                "    int var5 = java.lang.Math.min(this.e(var1.getConfigManager(), 30) / 40 * 6 + 14, 164);\n" +
                                "    java.lang.String var6 = this.a(var4, \"health\", var5);\n" +
                                "    com.threerings.opengl.gui.ay var7 = new com.threerings.opengl.gui.ay(var1, \"ui/window/tooltip_parts/stat_bar.dat\", \"Icon\", \"ui/icon/stats/shield_health.png\", new Object[]{\"Stat Label\", var2.get(\"t.health\"), \"Alignment\", \"Left\", \"Bonus Width\", Integer.valueOf(var5), \"Bonus Potential Width\", Integer.valueOf(0), \"Bar Type\", var6});\n" +
                                "    com.threerings.opengl.gui.Label _statLabel = ((com.threerings.opengl.gui.Label) var7.getComponent(\"stat_label\"));\n" +
                                "    _statLabel.setText(_statLabel.getText() + \" \" + Integer.toString(var5));\n" +
                                "    com.threerings.config.ConfigReference _styleConfigRef = new com.threerings.config.ConfigReference(\"Currency/Energy\");\n" +
                                "    com.threerings.opengl.gui.config.StyleConfig _styleConfig = (com.threerings.opengl.gui.config.StyleConfig) var1.getConfigManager().a(com.threerings.opengl.gui.config.StyleConfig.class, (com.threerings.config.ConfigReference) _styleConfigRef.clone());\n" +
                                "    com.threerings.opengl.gui.config.StyleConfig.Original _styleConfigOriginal = _styleConfig.getOriginal$45fbbce3(var1);\n" +
                                "    _styleConfigOriginal.color = new com.threerings.opengl.renderer.Color4f(179F / 255F, 238F / 255F, 58F / 255F, 1F);\n" +
                                "    _statLabel.setStyleConfigs(new com.threerings.opengl.gui.config.StyleConfig[]{_styleConfig});\n" +
                                "    var3.add(var7);\n" +
                                "    if (var4 != null) {\n" +
                                "        var4.put(\"health\", new com.threerings.projectx.item.config.ItemConfig.a(var7, var5, true));\n" +
                                "    }\n" +
                                "}"));
    }

    public static void main(String[] args) {
    }
}
