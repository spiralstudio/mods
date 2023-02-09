package com.spiralstudio.mod.pocketah;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * Type "/ah" to call the Auction House anytime and anywhere.
 *
 * @author Leego Yih
 * @see com.threerings.crowd.chat.client.a ChatDirector
 */
public class Main {
    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.crowd.chat.client.a");
            CtMethod ctMethod = ctClass.getDeclaredMethod("a", classPool.get(new String[]{"com.threerings.crowd.chat.client.m", "java.lang.String", "boolean"}));
            ctMethod.setBody("{\n" +
                    "                java.lang.String var4;\n" +
                    "                if ($2.equals(\"/ah\")) {\n" +
                    "                    com.threerings.projectx.util.A ctxxx = (com.threerings.projectx.util.A) this._ctx;\n" +
                    "                    com.threerings.projectx.client.aC hud = com.threerings.projectx.client.aC.h(ctxxx);\n" +
                    "                    com.threerings.projectx.auction.data.AuctionDialogInfo adi = new com.threerings.projectx.auction.data.AuctionDialogInfo();\n" +
                    "                    com.threerings.projectx.client.ff ah = adi.a(ctxxx, hud.vk());\n" +
                    "                    ctxxx.getRoot().addWindow(ah);\n" +
                    "                    return \"success\";\n" +
                    "                } else if (!$2.startsWith(\"/\")) {\n" +
                    "                    return com.samskivert.util.aq.Z(var4 = $2.trim()) ? \"success\" : this.a($1, var4, (byte) 0);\n" +
                    "                } else {\n" +
                    "                    var4 = $2.substring(1).toLowerCase();\n" +
                    "                    java.lang.String[] var5 = new java.lang.String[1];\n" +
                    "                    java.lang.String var6 = \"\";\n" +
                    "                    int var7;\n" +
                    "                    if ((var7 = $2.indexOf(\" \")) != -1) {\n" +
                    "                        var4 = $2.substring(1, var7).toLowerCase();\n" +
                    "                        var6 = $2.substring(var7 + 1).trim();\n" +
                    "                    }\n" +
                    "\n" +
                    "                    java.util.Map var14;\n" +
                    "                    switch ((var14 = this.aL(var4)).size()) {\n" +
                    "                        case 0:\n" +
                    "                            java.util.StringTokenizer var9 = new java.util.StringTokenizer($2);\n" +
                    "                            return com.threerings.util.N.m(\"m.unknown_command\", var9.nextToken());\n" +
                    "                        case 1:\n" +
                    "                            java.util.Map.Entry var11;\n" +
                    "                            java.lang.String var15 = (java.lang.String) (var11 = (java.util.Map.Entry) var14.entrySet().iterator().next()).getKey();\n" +
                    "                            java.lang.String var8;\n" +
                    "                            if (!(var8 = ((com.threerings.crowd.chat.client.a.c) var11.getValue()).a($1, var15, var6, var5)).equals(\"success\")) {\n" +
                    "                                return var8;\n" +
                    "                            }\n" +
                    "\n" +
                    "                            if ($3) {\n" +
                    "                                var5[0] = \"/\" + (var5[0] == null ? var4 : var5[0]);\n" +
                    "                                $2 = var5[0];\n" +
                    "                                this.EJ.remove($2);\n" +
                    "                                this.EJ.add($2);\n" +
                    "                                if (this.EJ.size() > 10) {\n" +
                    "                                    this.EJ.remove(0);\n" +
                    "                                }\n" +
                    "                            }\n" +
                    "\n" +
                    "                            return var8;\n" +
                    "                        default:\n" +
                    "                            java.lang.StringBuilder var10 = new java.lang.StringBuilder();\n" +
                    "                            java.util.Iterator var12 = com.google.common.collect.Sets.newTreeSet(var14.keySet()).iterator();\n" +
                    "\n" +
                    "                            while (var12.hasNext()) {\n" +
                    "                                java.lang.String var13 = (java.lang.String) var12.next();\n" +
                    "                                var10.append(\" /\").append(var13);\n" +
                    "                            }\n" +
                    "\n" +
                    "                            return com.threerings.util.N.m(\"m.unspecific_command\", var10.toString());\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }");
            ctClass.toClass();
            ctClass.detach();

            /*{
                java.lang.String var4;
                if ($2.equals("/ah")) {
                    com.threerings.projectx.util.A ctxxx = (com.threerings.projectx.util.A) this._ctx;
                    com.threerings.projectx.client.aC hud = com.threerings.projectx.client.aC.h(ctxxx);
                    com.threerings.projectx.auction.data.AuctionDialogInfo adi = new com.threerings.projectx.auction.data.AuctionDialogInfo();
                    com.threerings.projectx.client.ff ah = adi.a(ctxxx, hud.vk());
                    ctxxx.getRoot().addWindow(ah);
                    return "success";
                } else if (!$2.startsWith("/")) {
                    return com.samskivert.util.aq.Z(var4 = $2.trim()) ? "success" : this.a($1, var4, (byte) 0);
                } else {
                    var4 = $2.substring(1).toLowerCase();
                    java.lang.String[] var5 = new java.lang.String[1];
                    java.lang.String var6 = "";
                    int var7;
                    if ((var7 = $2.indexOf(" ")) != -1) {
                        var4 = $2.substring(1, var7).toLowerCase();
                        var6 = $2.substring(var7 + 1).trim();
                    }

                    java.util.Map var14;
                    switch ((var14 = this.aL(var4)).size()) {
                        case 0:
                            java.util.StringTokenizer var9 = new java.util.StringTokenizer($2);
                            return com.threerings.util.N.m("m.unknown_command", var9.nextToken());
                        case 1:
                            java.util.Map.Entry var11;
                            java.lang.String var15 = (java.lang.String) (var11 = (java.util.Map.Entry) var14.entrySet().iterator().next()).getKey();
                            java.lang.String var8;
                            if (!(var8 = ((com.threerings.crowd.chat.client.a.c) var11.getValue()).a($1, var15, var6, var5)).equals("success")) {
                                return var8;
                            }

                            if ($3) {
                                var5[0] = "/" + (var5[0] == null ? var4 : var5[0]);
                                $2 = var5[0];
                                this.EJ.remove($2);
                                this.EJ.add($2);
                                if (this.EJ.size() > 10) {
                                    this.EJ.remove(0);
                                }
                            }

                            return var8;
                        default:
                            java.lang.StringBuilder var10 = new java.lang.StringBuilder();
                            java.util.Iterator var12 = com.google.common.collect.Sets.newTreeSet(var14.keySet()).iterator();

                            while (var12.hasNext()) {
                                java.lang.String var13 = (java.lang.String) var12.next();
                                var10.append(" /").append(var13);
                            }

                            return com.threerings.util.N.m("m.unspecific_command", var10.toString());
                    }
                }
            }*/
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
