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
                    "System.out.println(\"ShopDialogInfo: \"+$0.aQw.toString());\n" +
                    "System.out.println(\"UniqueShopInfo: \"+$0.aQx.toString());\n" +
                    "com.threerings.projectx.shop.data.ShopDialogInfo sdi = $0.aQw;\n" +
                    "com.threerings.projectx.shop.data.UniqueShopInfo usi = $0.aQx;\n" +
                    "try {\n" +
                    "    Object name = com.threerings.projectx.shop.data.ShopDialogInfo.class.getField(\"name\").get(sdi);\n" +
                    "    Object title = com.threerings.projectx.shop.data.ShopDialogInfo.class.getField(\"title\").get(sdi);\n" +
                    "    System.out.println(\"[ShopDialog] shop name: \" + name + \", shop title: \" + title);\n" +
                    "    } catch (Exception e) {\n" +
                    "        throw new RuntimeException(e);\n" +
                    "    }\n" +
                    "for (int i = 0; i < usi.goodCounts.size(); i++) {\n" +
                    "    com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount gc = usi.goodCounts.get(i);\n" +
                    "    try {\n" +
                    "        Object good = com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount.class.getField(\"good\").get(gc);\n" +
                    "        Object count = com.threerings.projectx.shop.data.UniqueShopInfo.GoodCount.class.getField(\"count\").get(gc);\n" +
                    "        System.out.println(\"[ShopDialog] index: \" + Integer.toString(i) + \", good: \" + good.toString() +\", count: \" + count.toString());\n" +
                    "    } catch (Exception e) {\n" +
                    "        throw new RuntimeException(e);\n" +
                    "    }\n" +
                    "}\n" +
                    "return $0.aQx;\n" +
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
