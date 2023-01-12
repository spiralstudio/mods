# MiniMap

Enhance mini map.

## How it works

By decompiling [projectx-pcode.jar](src/lib/projectx-pcode.jar) we can find that
`Minimap` defines three ping thresholds (`100`, `150`, `250`) and four icons (`"4_optimal"`, `"3_good"`, `"2_moderate"`, `"1_poor"`).

We are going to use the ping value instead of the icons.

```java
public class Minimap extends ay implements aC.b, ProjectXCodes {
    private static int[] aoW = new int[]{100, 150, 250};
    private static String[] aoX = new String[]{"4_optimal", "3_good", "2_moderate", "1_poor"};

    public final void bG(int var1) {
        int var2;
        for(var2 = 0; var2 < aoW.length && var1 > aoW[var2]; ++var2) {
        }

        if (this.aoR != var2) {
            if (this.aoR != -1 && Math.abs(this.aoR - var2) == 1) {
                if (var2 > this.aoR) {
                    if (var1 < aoW[this.aoR] + 15) {
                        return;
                    }
                } else if (var1 > aoW[var2] - 15) {
                    return;
                }
            }

            this.aoQ.setIcon(com.threerings.miso.client.ResolutionView.a.a(this._ctx, "ui/minimap/connection/" + aoX[this.aoR = var2] + ".png"));
        }
    }
}
```

Use `ByteBuddy` to replace the method and just display the ping value.

```java
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
```
