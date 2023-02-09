package com.spiralstudio.mod.pocketshop;

import com.threerings.projectx.client.ProjectXApp;

import java.lang.reflect.Method;

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
            Method add = Class.forName("com.spiralstudio.mod.command.Command")
                .getDeclaredMethod("add", String.class, String.class);

            add.invoke(null, "ah", "\n" +
                "com.threerings.projectx.util.A ctxxx = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud = com.threerings.projectx.client.aC.h(ctxxx);\n" +
                "com.threerings.projectx.auction.data.AuctionDialogInfo adi = new com.threerings.projectx.auction.data.AuctionDialogInfo();\n" +
                "com.threerings.projectx.client.ff ah = adi.a(ctxxx, hud.vk());\n" +
                "ctxxx.getRoot().addWindow(ah);\n");

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
