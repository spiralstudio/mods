package com.spiralstudio.mod.forceautotarget;

import com.spiralstudio.mod.core.util.ClassBuilder;
import com.spiralstudio.mod.core.util.ConstructorModifier;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.dungeon.data.DungeonInputFrame
 */
public class Main {
    private static boolean mounted = false;

    public static void mount() throws Exception {
        if (mounted) {
            return;
        }
        mounted = true;
        redefineDungeonInputFrame();
    }
    static void redefineDungeonInputFrame() throws Exception {
        ClassBuilder.fromClass("com.threerings.projectx.dungeon.data.DungeonInputFrame")
                .modifyConstructor(new ConstructorModifier()
                        .paramTypeNames("int", "float", "float", "int", "int")
                        .insertBefore("" +
                                "boolean autoTarget = (($4 & 512) != 0);\n" +
                                "if (!autoTarget) {$4 |= 512;}\n"))
                .build();
    }

    public static void main(String[] args) {
    }
}
