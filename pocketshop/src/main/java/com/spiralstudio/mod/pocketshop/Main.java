package com.spiralstudio.mod.pocketshop;

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
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 */
public class Main {
    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.crowd.chat.client.a");
            CtMethod ctMethod = ctClass.getDeclaredMethod("a", classPool.get(new String[]{"com.threerings.crowd.chat.client.m", "java.lang.String", "boolean"}));
            ctMethod.insertBefore("" +
                    "if ($2.equals(\"/ah\")) {\n" +
                    "    com.threerings.projectx.util.A ctxxx = (com.threerings.projectx.util.A) this._ctx;\n" +
                    "    com.threerings.projectx.client.aC hud = com.threerings.projectx.client.aC.h(ctxxx);\n" +
                    "    com.threerings.projectx.auction.data.AuctionDialogInfo adi = new com.threerings.projectx.auction.data.AuctionDialogInfo();\n" +
                    "    com.threerings.projectx.client.ff ah = adi.a(ctxxx, hud.vk());\n" +
                    "    ctxxx.getRoot().addWindow(ah);\n" +
                    "    return \"success\";\n" +
                    "}");
            ctClass.toClass();
            ctClass.detach();

            /*if ($2.equals("/ah")) {
                com.threerings.projectx.util.A ctxxx = (com.threerings.projectx.util.A) this._ctx;
                com.threerings.projectx.client.aC hud = com.threerings.projectx.client.aC.h(ctxxx);
                com.threerings.projectx.auction.data.AuctionDialogInfo adi = new com.threerings.projectx.auction.data.AuctionDialogInfo();
                com.threerings.projectx.client.ff ah = adi.a(ctxxx, hud.vk());
                ctxxx.getRoot().addWindow(ah);
                return "success";
            }*/
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
