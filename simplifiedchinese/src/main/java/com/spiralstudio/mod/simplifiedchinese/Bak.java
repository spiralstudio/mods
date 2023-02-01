package com.spiralstudio.mod.simplifiedchinese;

import com.threerings.projectx.client.ProjectXApp;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Properties;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.util.a#Nm
 * @see com.samskivert.util.m#b(String, String[])
 * @see com.threerings.projectx.client.cs.a
 */
public class Bak {
    static {
        try {
            //2023/02/01 13:05:30:956 WARNING K$a.b: Could not read news image [key=left.image, url=http://content.spiralknights.com/images/rank_10/rank_10-2_uplink-sml_zh.png]
            //javax.imageio.IIOException: Can't get input stream from URL!
            //	at javax.imageio.ImageIO.read(ImageIO.java:1395)
            //	at com.threerings.projectx.client.cs$a.a(SourceFile:304)
            //	at com.threerings.projectx.client.cs$a.fZ(SourceFile:267)
            //	at com.samskivert.util.H.fF(SourceFile:210)
            //	at com.samskivert.util.V.run(SourceFile:60)
            ClassPool classPool = ClassPool.getDefault();
            classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
            CtClass ctClass = classPool.get("com.threerings.projectx.client.cs$a");
            CtMethod ctMethod = ctClass.getDeclaredMethod("a");
            ctMethod.setBody("{\n" +
                    "            String var3;\n" +
                    "            if ((var3 = $0.getProperty($1)) != null) {\n" +
                    "                java.net.URL var6 = com.threerings.projectx.util.a.dl(var3.replaceAll(\"\\\\{lang\\\\}\", \"en\"));\n" +
                    "                try {\n" +
                    "                    return javax.imageio.ImageIO.read(var6);\n" +
                    "                } catch (java.lang.Throwable var5) {\n" +
                    "                    if (com.threerings.projectx.util.a.MP() && $0.j(var5)) {\n" +
                    "                        com.threerings.projectx.a.log.f(\"Could not read news image\", new Object[]{\"key\", $1, \"url\", var6});\n" +
                    "                    } else {\n" +
                    "                        com.threerings.projectx.a.log.f(\"Could not read news image\", new Object[]{\"key\", $1, \"url\", var6, var5});\n" +
                    "                        $2[0] = true;\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            }\n" +
                    "            return null;\n" +
                    "        }");
            ctClass.toClass();
            ctClass.detach();

            // com.threerings.projectx.util.DeploymentConfig
            Field configField = com.threerings.projectx.util.a.class.getDeclaredField("akf");
            configField.setAccessible(true);
            Object config = configField.get(null);
            // com.samskivert.util.Config
            Field propsField = com.samskivert.util.m.class.getDeclaredField("AQ");
            propsField.setAccessible(true);
            Properties props = (Properties) propsField.get(config);
            // Add a new locale "zh"
            String newLocale = Locale.CHINESE.getLanguage();
            String supportedLocales = props.getProperty("supported_locales");
            props.put("supported_locales", String.format("%s, %s", newLocale, supportedLocales));
            // Modify default locale
            props.put("default_locale", newLocale);
            System.out.printf("Supported locales: %s, additional locale: %s, default locale: %s%n", supportedLocales, newLocale, newLocale);
        } catch (Throwable cause) {
            throw new Error(cause);
        }
    }

    public static void main(String[] args) {
        ProjectXApp.main(args);
    }
}
