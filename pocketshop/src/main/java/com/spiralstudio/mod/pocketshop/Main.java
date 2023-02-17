package com.spiralstudio.mod.pocketshop;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;
import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

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
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        // TODO It doesn't work
        //redefineForgeDialogToEnableButton();
        //redefineCrafterDialogToEnableButton();
        addShopCommands();
    }

    static void redefineForgeDialogToEnableButton() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.item.client.n")
                .modifyMethod(new MethodModifier()
                        .methodName("HX")
                        .insertAfter("this.aJN.setEnabled(true);\n"))
                .build();
    }

    static void redefineCrafterDialogToEnableButton() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.craft.a.s")
                .modifyMethod(new MethodModifier()
                        .methodName("sA")
                        .insertAfter("this.atz.setEnabled(true);\nthis.atz.setVisible(true);\n"))
                .build();
    }

    static void addShopCommands() {
        // Auction House
        Commands.addCommand("ah", "\n" +
                "com.threerings.projectx.auction.data.AuctionDialogInfo di = new com.threerings.projectx.auction.data.AuctionDialogInfo();\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        /*// UV - Punch
        Command.addCommand("uv|punch", "\n" +
                "com.threerings.projectx.craft.data.VariantMakerDialogInfo di = new com.threerings.projectx.craft.data.VariantMakerDialogInfo();\n" +
                "di.name = \"m.haven_punch\";\n" +
                "di.title = \"t.haven_punch\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Unbind - Vice
        Command.addCommand("unbind|vice", "\n" +
                "com.threerings.projectx.craft.data.UnbindDialogInfo di = new com.threerings.projectx.craft.data.UnbindDialogInfo();\n" +
                "di.name = \"m.haven_vice\";\n" +
                "di.title = \"t.haven_vice\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Accessory - Bechamel
        Command.addCommand("accessory", "\n" +
                "com.threerings.projectx.craft.data.AccessoryDialogInfo di = new com.threerings.projectx.craft.data.AccessoryDialogInfo();\n" +
                "di.name = \"m.accessory\";\n" +
                "di.title = \"t.accessory\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Harness
        Command.addCommand("harness", "\n" +
                "com.threerings.projectx.sprites.data.HarnessDialogInfo di = new com.threerings.projectx.sprites.data.HarnessDialogInfo();\n" +
                "di.name = \"m.harness_vendor\";\n" +
                "di.title = \"t.harness_vendor\";\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Craft
        Command.addCommand("craft", "\n" +
                "com.threerings.projectx.craft.data.CrafterDialogInfo di = new com.threerings.projectx.craft.data.CrafterDialogInfo();\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "ctx__.getRoot().addWindow(di.a(ctx__, hud__.vk()));\n");
        // Forge
        Command.addCommand("forge|heat", "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.item.client.q win__ = new com.threerings.projectx.item.client.q(ctx__, null);\n");*/
    }

    public static void main(String[] args) {
    }
}
