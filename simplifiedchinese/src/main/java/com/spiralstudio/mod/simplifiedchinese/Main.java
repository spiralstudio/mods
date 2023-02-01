package com.spiralstudio.mod.simplifiedchinese;

import com.threerings.projectx.client.ProjectXApp;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Properties;

/**
 * @author Leego Yih
 * @see com.threerings.projectx.util.a#Nm
 * @see com.samskivert.util.m#b(String, String[])
 */
public class Main {
    static {
        try {
            // com.threerings.projectx.util.DeploymentConfig
            Field configField = com.threerings.projectx.util.a.class.getDeclaredField("akf");
            configField.setAccessible(true);
            Object config = configField.get(null);
            // com.samskivert.util.Config
            Field propsField = com.samskivert.util.m.class.getDeclaredField("AQ");
            propsField.setAccessible(true);
            Properties props = (Properties) propsField.get(config);
            // Add a new locale "zh"
            String newLocale = Locale.SIMPLIFIED_CHINESE.getLanguage();
            String supportedLocales = props.getProperty("supported_locales");
            props.put("supported_locales", String.format("%s, %s", supportedLocales, newLocale));
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