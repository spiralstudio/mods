package com.spiralstudio.minimap;

import com.threerings.opengl.gui.Label;
import com.threerings.projectx.client.ProjectXApp;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * Overrides the `notePing` method of the class `Minimap` to display ping value.
 *
 * @author Leego Yih
 * @see com.threerings.projectx.client.hud.Minimap
 */
public class Main {

    static {
        try {
            ByteBuddyAgent.install();
            new AgentBuilder.Default()
                    .type(ElementMatchers.named("com.threerings.projectx.client.hud.Minimap"))
                    .transform((builder, typeDescription, classLoader, module, domain) -> builder
                            .method(ElementMatchers.named("bG"))
                            .intercept(InvocationHandlerAdapter.of((proxy, method, args) -> {
                                Object ping = proxy.getClass().getField("aoQ").get(proxy);
                                if (ping == null) {
                                    System.out.println("No ping label, should not happen!");
                                    return null;
                                }
                                ((Label) ping).setText(args[0] + "ms");
                                return null;
                            })))
                    .installOnByteBuddyAgent();
        } catch (Throwable cause) {
            throw new Error("Failed to load Minimap", cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
