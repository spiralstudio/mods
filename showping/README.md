# ShowPing

Show ping value on the minimap.

![ping](ping.png)

## How it works

By decompiling [projectx-pcode.jar](../lib/projectx-pcode.jar)
we can find that `Minimap` defines three ping values (`100`, `150`, `250`)
and four icons (`"4_optimal"`, `"3_good"`, `"2_moderate"`, `"1_poor"`).

We are going to use the ping value instead of the icons.

```java
public class Minimap extends ay implements aC.b, ProjectXCodes {
    private static int[] aoW = new int[]{100, 150, 250};
    private static String[] aoX = new String[]{"4_optimal", "3_good", "2_moderate", "1_poor"};

    public final void bG(int var1) {
        int var2;
        for (var2 = 0; var2 < aoW.length && var1 > aoW[var2]; ++var2) {
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

Use `Javassist` to replace the method and display the ping value.

```java
ClassPool classPool=ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        CtClass ctClass=classPool.get("com.threerings.projectx.client.hud.Minimap");
        CtMethod ctMethod=ctClass.getDeclaredMethod("bG");
        ctMethod.setBody("{$0.aoQ.setIcon(null);$0.aoQ.setText(Integer.toString($1) + \"ms\");}");
        ctClass.toClass();
        ctClass.detach();
```
