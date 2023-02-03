package com.spiralstudio.mod.pandorabox;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.item.client.ArsenalPanel
 * @see com.threerings.projectx.shop.client.l ShopDialog
 * @see com.threerings.projectx.shop.data.ShopDialogInfo
 * @see com.threerings.projectx.shop.data.UniqueShopInfo
 */
public class Main {
    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.projectx.shop.client.l");
            CtMethod ctMethod = ctClass.getDeclaredMethod("KD");
            ctMethod.setBody("{" +
                    "com.threerings.projectx.shop.data.ShopDialogInfo sdi = this.aQw;\n" +
                    "com.threerings.projectx.shop.data.UniqueShopInfo usi = this.aQx;\n" +
                    "System.out.println(\"[ShopDialog] shop name: \" + sdi.name + \", shop title: \" + sdi.title);\n" +
                    "for (int i = 0; i < usi.goodCounts.size(); i++) {\n" +
                    "    com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount gc = usi.goodCounts.get(i);\n" +
                    "    System.out.println(\"[ShopDialog] index: \" + Integer.toString(i) + \", good: \" + gc.good.toString() +\", count: \" + Integer.toString(gc.count));\n" +
                    "}\n" +
                    "return this.aQx;\n" +
                    "}");
            ctClass.toClass();
            ctClass.detach();
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
