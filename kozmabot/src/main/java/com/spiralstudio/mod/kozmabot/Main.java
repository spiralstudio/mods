package com.spiralstudio.mod.kozmabot;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.auction.client.b AuctionService
 * @see com.threerings.projectx.auction.client.a auctionhouse
 * @see com.threerings.projectx.auction.client.SearchPanel
 * @see com.threerings.projectx.auction.client.SearchPanel#by addPage
 * @see com.threerings.projectx.auction.client.SearchPanel#bz searchWithPage
 * @see com.threerings.projectx.auction.client.e listings
 * @see com.threerings.projectx.auction.client.e#h updateListings
 * @see com.threerings.projectx.auction.client.F mybids
 * @see com.threerings.projectx.auction.client.r myauctions
 */
public class Main {
    static {
        try {
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.tudey.a.k");
            CtMethod ctMethod = ctClass.getDeclaredMethod("c", classPool.get(new String[]{"com.threerings.crowd.data.PlaceObject"}));
            ctMethod.setBody("{\n" +
                    "                this.aTX = (com.threerings.tudey.data.TudeySceneObject) $1;\n" +
                    "                this.aqE.rs().a(this);\n" +
                    "                this.aqE.rt().a(this);\n" +
                    "                com.threerings.tudey.data.TudeySceneModel model = (com.threerings.tudey.data.TudeySceneModel) this.aqE.ru().QY().Rf();\n" +
                    "                this.aYd = this.ul();\n" +
                    "                if (this.aYd == null) {\n" +
                    "                    this.c(model);\n" +
                    "                } else {\n" +
                    "                    this.U(0.0F);\n" +
                    "                    System.gc();\n" +
                    "                    System.runFinalization();\n" +
                    "                }\n" +
                    "            }");
            ctClass.toClass();
            ctClass.detach();

            /*{
                this.aTX = (com.threerings.tudey.data.TudeySceneObject) $1;
                this.aqE.rs().a(this);
                this.aqE.rt().a(this);
                com.threerings.tudey.data.TudeySceneModel model = (com.threerings.tudey.data.TudeySceneModel) this.aqE.ru().QY().Rf();
                this.aYd = this.ul();
                if (this.aYd == null) {
                    this.c(model);
                } else {
                    this.U(0.0F);
                    System.gc();
                    System.runFinalization();
                }
            }*/
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    public static void main(String[] args) {
    }
}
