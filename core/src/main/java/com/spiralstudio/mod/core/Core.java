package com.spiralstudio.mod.core;

/**
 * @author Leego Yih
 */
public class Core {
    public static void mount() throws Exception {
        Registers.init();
        Commands.init();
        ClassPool.init();
    }

    public static void main(String[] args) {
    }
}
