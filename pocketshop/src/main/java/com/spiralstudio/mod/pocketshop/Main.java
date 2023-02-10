package com.spiralstudio.mod.pocketshop;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.reflect.Method;

/**
 * Enter commands to call shops anytime and anywhere.
 * <ul>
 *     <li>Enter "/ah" to call the Auction House.</li>
 *     <li>Enter "/uv" or "/punch" to call the Punch.</li>
 *     <li>Enter "/unbind" or "/vice" to call the Vice.</li>
 *     <li>Enter "/accessory" to call the Bechamel.</li>
 *     <li>Enter "/harness" to call the Bechamel.</li>
 *     <li>Enter "/forge" or "/heat" to call the Forge.</li>
 * </ul>
 *
 * @author Leego Yih
 * @see com.threerings.crowd.chat.client.a ChatDirector
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 */
public class Main {
    static {
        try {
            // TODO It doesn't work
            //redefineForgeWindowToEnableButton();
            addShopChatCommands();
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    static void redefineForgeWindowToEnableButton() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass = classPool.get("com.threerings.projectx.item.client.n");
        CtMethod ctMethod = ctClass.getDeclaredMethod("HX");
        ctMethod.insertAfter("" +
                "if (this.aJU >= this.aJX.Im() && this.aJT >= this.aJU) {\n" +
                "    this.aJN.setEnabled(true);\n" +
                "}");
        ctClass.toClass();
        ctClass.detach();
    }

    static void addShopChatCommands() throws Exception {
        Method add = Class.forName("com.spiralstudio.mod.command.Command")
                .getDeclaredMethod("add", String.class, String.class);
        // Auction House
        add.invoke(null, "ah", "\n" +
                "com.threerings.projectx.auction.data.AuctionDialogInfo di = new com.threerings.projectx.auction.data.AuctionDialogInfo();\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // UV - Punch
        add.invoke(null, "uv|punch", "\n" +
                "com.threerings.projectx.craft.data.VariantMakerDialogInfo di = new com.threerings.projectx.craft.data.VariantMakerDialogInfo();\n" +
                "di.name = \"m.haven_punch\";\n" +
                "di.title = \"t.haven_punch\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Unbind - Vice
        add.invoke(null, "unbind|vice", "\n" +
                "com.threerings.projectx.craft.data.UnbindDialogInfo di = new com.threerings.projectx.craft.data.UnbindDialogInfo();\n" +
                "di.name = \"m.haven_vice\";\n" +
                "di.title = \"t.haven_vice\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Accessory - Bechamel
        add.invoke(null, "accessory", "\n" +
                "com.threerings.projectx.craft.data.AccessoryDialogInfo di = new com.threerings.projectx.craft.data.AccessoryDialogInfo();\n" +
                "di.name = \"m.accessory\";\n" +
                "di.title = \"t.accessory\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Harness
        add.invoke(null, "harness", "\n" +
                "com.threerings.projectx.sprites.data.HarnessDialogInfo di = new com.threerings.projectx.sprites.data.HarnessDialogInfo();\n" +
                "di.name = \"m.harness_vendor\";\n" +
                "di.title = \"t.harness_vendor\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Craft
        add.invoke(null, "craft", "\n" +
                "com.threerings.projectx.craft.data.CrafterDialogInfo di = new com.threerings.projectx.craft.data.CrafterDialogInfo();\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Forge
        add.invoke(null, "forge|heat", "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.item.client.q win__ = new com.threerings.projectx.item.client.q(ctx__, null);\n");
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
