# StayOnline

Make knights never sleep!

## How it works

By decompiling [projectx-pcode.jar](src/lib/projectx-pcode.jar) we can find that `c` is the `IdleTracker`,
so we just need to disable the `start()` method.

```java
public class ProjectXApp extends n implements ProjectXCodes, A {

    public class c extends E implements com.threerings.opengl.gui.event.d {
        private long Rk;
        private boolean alw;

        public c() {
            super(ProjectXApp.this.getRunQueue());
        }

        public final void start() {
            this.Rk = System.currentTimeMillis();
            ProjectXApp.this.akx.addGlobalEventListener(this);
            this.schedule(10000L, true);
        }

        public final void eventDispatched(Event var1) {
            this.o(false);
            long var2 = var1.getWhen();
            this.Rk = var2 > 0L ? var2 : System.currentTimeMillis();
        }

        public final void expired() {
            long var1;
            if ((var1 = System.currentTimeMillis() - this.Rk) >= 180000L && !ProjectXApp.this.akw.JP()) {
                this.o(true);
                PlayerObject var3 = ProjectXApp.this.uk();
                if (ProjectXApp.this.UO.isLoggedOn() && !var3.tokens.Cg() && var1 >= (var3.isAnonymous() ? 3600000L : 900000L)) {
                    com.threerings.projectx.a.log.e("Client idled out.", new Object[0]);
                    ProjectXApp.this.akK = "m.idled_out";
                    ProjectXApp.this.UO.D(false);
                }

            }
        }

        private void o(boolean var1) {
            if (this.alw != var1) {
                com.threerings.projectx.a.log.e("Idle state changed.", new Object[]{"idle", this.alw = var1});
                if (ProjectXApp.this.UO.isLoggedOn()) {
                    ((com.threerings.crowd.client.a) ProjectXApp.this.UO.aR(com.threerings.crowd.client.a.class)).o(this.alw);
                }

            }
        }
    }
}
```

Use `Javassist` to replace the method.

```java
ClassPool classPool = ClassPool.getDefault();
classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
CtClass ctClass = classPool.get("com.threerings.projectx.client.ProjectXApp$c");
CtMethod ctMethod = ctClass.getDeclaredMethod("start");
ctMethod.setBody("System.out.println(\"Never sleep\");");
ctClass.toClass();
ctClass.detach();
```
