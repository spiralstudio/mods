package com.spiralstudio.mod.stayonline;

import com.spiralstudio.mod.core.ClassPool;
import com.spiralstudio.mod.core.util.MethodModifier;

/**
 * Overrides the `start` method of the class `IdleTracker` to disable it.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.ProjectXApp.c IdleTracker
 */
public class Main {
    private static boolean mounted = false;

    static {
    }

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        ClassPool.from("com.threerings.projectx.client.ProjectXApp$c")
                .modifyMethod(new MethodModifier()
                        .methodName("start")
                        .body("{System.out.println(\"[StayOnline] Never Idle\");}"));
    }

    public static void main(String[] args) {
    }
}
