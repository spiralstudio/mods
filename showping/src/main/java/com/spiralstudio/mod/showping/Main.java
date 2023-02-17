package com.spiralstudio.mod.showping;

import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * Overrides the `notePing` method of the class `Minimap` to show ping value.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.hud.Minimap
 */
public class Main {
    private static boolean mounted = false;

    static {
    }

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        ClassBuilder.fromClass("com.threerings.projectx.client.hud.Minimap")
                .modifyMethod(new MethodModifier()
                        .methodName("bG")
                        .body("{$0.aoQ.setIcon(null);$0.aoQ.setText(Integer.toString($1));}"))
                .build();
    }

    public static void main(String[] args) {
    }
}
