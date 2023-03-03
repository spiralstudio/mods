package com.spiralstudio.mod.test;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ConstructorModifier;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * @author Leego Yih
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
        //testMonsterHudBar();
        //testAdminCommand();
        //testMap();
        //testBaseItemListPanel();
        test();
    }

    static void testMonsterHudBar() {
        ClassPool.from("com.threerings.projectx.dungeon.client.g")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .insertAfter("this.aov.setVisible(true);\n"));
    }

    static void testAdminCommand() {
        // Does anyone have a token greater than zero?
        ClassPool.from("com.threerings.crowd.data.TokenRing")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("int")
                        .insertBefore("System.out.println(\"token :\" + $1);"))
                .modifyMethod(new MethodModifier()
                        .methodName("aw")
                        .body("{return true;}"))
                .modifyMethod(new MethodModifier()
                        .methodName("ax")
                        .body("{return true;}"))
                .modifyMethod(new MethodModifier()
                        .methodName("iD")
                        .body("{return true;}"));
    }

    static void testMap() {
        // Show monsters on map
        ClassPool.from("com.threerings.projectx.map.client.a")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("com.threerings.tudey.util.m", "com.threerings.tudey.a.k")
                        .insertAfter("" +
                                "this.aMi.dY(ProjectXCodes.avh | MaskEditor.n(\"collision\", \"player\") | MaskEditor.n(\"collision\", \"player_barrier\") | MaskEditor.n(\"collision\", \"monster\"));\n"));
        // Zoom map
        ClassPool.from("com.threerings.projectx.map.client.c")
                .modifyMethod(new MethodModifier()
                        .methodName("tick")
                        .insertAfter("" +
                                "if (this.aMq != null) {\n" +
                                "    com.threerings.opengl.gui.e.c var3 = this.aMq.getPreferredSize(-1, -1);\n" +
                                "    var3.height = this.aqE.getRoot().getDisplayHeight() * 2;\n" +
                                "    var3.width = this.aqE.getRoot().getDisplayWidth() * 2;\n" +
                                "    this.aMq.setPreferredSize(var3);\n" +
                                "}"));
    }

    static void testBaseItemListPanel() {
        ClassPool.from("com.threerings.projectx.item.client.j")
                .modifyMethod(new MethodModifier()
                        .methodName("d")
                        .paramTypeNames("com.threerings.projectx.item.client.j$b")
                        .body("{" +
                                "com.threerings.opengl.gui.ay _pill = a(this._ctx, $1.getName(), $1.Bo(), $1.getReference(), $1.Bf(), this.Bn(), this.HR(), false);" +
                                "System.out.println(_pill);" +
                                "return _pill;" +
                                "}"));
    }

    static void test() {
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
                                "    int var25;\n" +
                                "    for (var25 = 0; var25 < var24; ++var25) {\n" +
                                "        com.threerings.projectx.dungeon.util.DamageSummary.DamageType var14 = var22[var25];\n" +
                                "        float _curDmg = ((com.threerings.expr.ag) var20.aEa.bc(var14)).value;\n" +
                                "        float _maxDmg = ((com.threerings.expr.ag) var21.aEa.bc(var14)).value;\n" +
                                "        float var15 = _curDmg * 0.41666666F;\n" +
                                "        float var16 = _maxDmg * 0.41666666F;\n" +
                                "        boolean var17 = var15 > 0.0F || var16 > 0.0F;\n" +
                                "        if (com.threerings.projectx.item.config.ItemConfig.Level.a(var5, (java.lang.Object) var14, var17)) {\n" +
                                "            java.lang.String var18 = var14.toString().toLowerCase();\n" +
                                "            int var27 = Math.min((int) (var15 > 0.0F ? var15 + 14.0F : 0.0F), 164);\n" +
                                "            int var29 = Math.min((int) (var16 > 0.0F ? var16 + 14.0F : 0.0F), 164);\n" +
                                "            java.lang.String var19 = this.a(var5, var14, var27);\n" +
                                "            com.threerings.opengl.gui.ay var30 = new com.threerings.opengl.gui.ay(var1, \"ui/window/tooltip_parts/stat_bar.dat\", \"Icon\", \"ui/icon/stats/attack_\" + var18 + \".png\", new java.lang.Object[]{\"Stat Label\", var3.get(\"t.\" + var18), \"Alignment\", \"Left\", \"Bonus Width\", Integer.valueOf(var27), \"Bonus Potential Width\", Integer.valueOf(var29), \"Bar Type\", var19});\n" +
                                "            com.threerings.opengl.gui.Label _statLabel = ((com.threerings.opengl.gui.Label) var30.getComponent(\"stat_label\"));\n" +
                                "            if (_curDmg == _maxDmg) {\n" +
                                "                _statLabel.setText(_statLabel.getText() + \" \" + Integer.toString((int) _curDmg));\n" +
                                "            } else {\n" +
                                "                _statLabel.setText(_statLabel.getText() + \" \" + Integer.toString((int) _curDmg) + \"/\" + Integer.toString((int) _maxDmg));\n" +
                                "            }\n" +
                                "            System.out.println(\" StyleConfigbefore:\" + java.util.Arrays.toString(_statLabel.getStyleConfigs()));\n" +
                                "            _statLabel.setStyleConfig(\"Currency/Crowns\");\n" +
                                "            System.out.println(\" StyleConfigafter:\" + java.util.Arrays.toString(_statLabel.getStyleConfigs()));\n" +
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
                                "    _statLabel.setStyleConfig(\"Currency/Crowns\");\n" +
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

    public String printStackTrace() {
        java.lang.Throwable source = new java.lang.Throwable();
        java.lang.StackTraceElement[] es = source.getStackTrace();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (es != null) {
            for (int i = 0; i < es.length; i++) {
                StackTraceElement e = es[i];
                sb.append(e.getClassName());
                sb.append(".").append(e.getMethodName());
                sb.append("(").append(e.getFileName()).append(":");
                sb.append(e.getLineNumber()).append(")").append("\n");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    }
}
