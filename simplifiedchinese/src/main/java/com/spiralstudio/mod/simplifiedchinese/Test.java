package com.spiralstudio.mod.simplifiedchinese;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Test {
    static String source = "/Users/dasouche/Frameworks/projectx-config/lib/projectx-config.jar.src/rsrc";
    static String target = "/Users/dasouche/Frameworks/projectx-config/resources/test/rsrc";

    public static void main(String[] args) throws Exception {
        //   rename(new File(source));
        String rsrc = "/Users/dasouche/Frameworks/mods/simplifiedchinese/src/main/resources/rsrc";
        sort(new File(rsrc));
    }

    public static void rename(File file) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                for (File subFile : subFiles) {
                    rename(subFile);
                }
            }
            return;
        }
        if (file.getName().endsWith("_de.properties")) {
            File parent = new File(file.getParent().replace(source, target));
            if (!parent.exists()) {
                parent.mkdirs();
            }
            file.renameTo(new File(parent.getAbsolutePath() + "/" + file.getName().replace("_de.properties", "_zh.properties")));
        }
    }

    public static void sort(File file) throws Exception {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null && subFiles.length > 0) {
                for (File subFile : subFiles) {
                    sort(subFile);
                }
            }
            return;
        }
        if (file.getName().endsWith("_zh.properties")) {
            String s = readFile(file);
            if (s == null) {
                return;
            }
            StringBuilder sb = new StringBuilder();
            List<String> sorted = new ArrayList<>();
            String[] lines = s.split("\n");
            for (String line : lines) {
                if (line.startsWith("#")) {

                } else if (line.length() > 0) {
                    sorted.add(line);
                }
            }
            sorted.sort(String::compareTo);
            for (String s1 : sorted) {
                sb.append(s1).append("\n");
            }
            writeFile(file, sb.toString());
        }
    }

    static String readFile(File file) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            System.out.println("Failed to read '" + file.getName() + "'");
            e.printStackTrace();
            return null;
        }
    }

    static void writeFile(File file, String s) throws Exception {
        FileWriter writer = new FileWriter(file.getAbsoluteFile());
        writer.write(s);
        writer.flush();
        writer.close();
    }
}
