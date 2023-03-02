package com.spiralstudio.mod.teleport;

import com.spiralstudio.mod.core.Commands;
import com.spiralstudio.mod.core.Registers;

/**
 * Enter commands to go somewhere.
 *
 * @author Leego Yih
 * @see com.threerings.crowd.chat.client.a ChatDirector
 * @see com.threerings.crowd.chat.client.a.c CommandHandler
 * @see com.threerings.projectx.client.aC HudWindow
 * @see com.threerings.projectx.client.hud.a Activities
 * @see com.threerings.projectx.mission.client.MissionPanel
 * @see com.threerings.projectx.mission.client.u MissionWindow I guess?
 */
public class Main {
    private static boolean mounted = false;

    static {
        Registers.add(Main.class);
    }

    public static void mount() {
        if (mounted) {
            return;
        }
        mounted = true;
        addTeleportChatCommands();
    }

    static void addTeleportChatCommands() {
        // Go to Haven
        Commands.addCommand("haven|hh", "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "((com.threerings.projectx.client.bh)ctx__.rq().aR(com.threerings.projectx.client.bh.class)).vG();\n");
        // Go to Ready Room
        Commands.addCommand("readyroom|rr", "\n" +
                "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "java.lang.reflect.Field afuField = com.threerings.projectx.client.aC.class.getDeclaredField(\"afu\");\n" +
                "afuField.setAccessible(true);\n" +
                "com.threerings.projectx.client.hud.a a__ = (com.threerings.projectx.client.hud.a) afuField.get(hud__);\n" +
                "java.lang.reflect.Field anNField = com.threerings.projectx.client.hud.a.class.getDeclaredField(\"anN\");\n" +
                "anNField.setAccessible(true);\n" +
                "com.threerings.opengl.gui.q rr__ = (com.threerings.opengl.gui.q) anNField.get(a__);\n" +
                "Class.forName(\"com.threerings.opengl.gui.e.a\")\n" +
                "    .getDeclaredMethod(\"postAction\", new Class[]{com.threerings.opengl.gui.q.class, java.lang.String.class})\n" +
                "    .invoke(null, new Object[]{rr__, \"readyroom\"});");

        // Go to Town Square
        Commands.addCommand("townsquare|ts", doScene("1"));
        // Go to Bazaar
        Commands.addCommand("bazaar|ba", doScene("2"));
        // Go to Garrison
        Commands.addCommand("garrison|ga", doScene("445"));
        // Go to Arcade
        Commands.addCommand("arcade|ar", doScene("3"));

        // Go to FSC
        Commands.addCommand("fsc|vana", doMission("king_of_ashes"));
        // Go to Jelly King
        Commands.addCommand("jk|rjp", doMission("sovereign_slime"));
        // Go to Built to Destroy
        Commands.addCommand("imf|twins", doMission("built_to_destroy"));
        // Go to DaN
        Commands.addCommand("dan", doMission("dreams_and_nightmares"));
        // Go to Axes of Evil
        Commands.addCommand("aoe", doMission("axes_of_evil"));
        // Go to Shadowplay
        Commands.addCommand("sp", doMission("shadowplay"));
    }

    static String doMission(String name) {
        return "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "com.threerings.projectx.client.aC hud__ = com.threerings.projectx.client.aC.h(ctx__);\n" +
                "com.threerings.projectx.mission.client.MissionPanel missionPanel__ = new com.threerings.projectx.mission.client.MissionPanel(ctx__, null);\n" +
                "java.lang.String mission = \"" + name + "\";\n" +
                "com.threerings.projectx.dungeon.data.DungeonCodes.Difficulty difficulty = com.threerings.projectx.dungeon.data.DungeonCodes.Difficulty.HARD;\n" +
                "java.lang.reflect.Constructor constructor = Class.forName(\"com.threerings.projectx.mission.client.f\").getDeclaredConstructors()[0];\n" +
                "constructor.setAccessible(true);\n" +
                "com.threerings.presents.client.D di = (com.threerings.presents.client.D) constructor\n" +
                "        .newInstance(new Object[]{missionPanel__, ctx__, \"board\", com.threerings.projectx.client.ProjectXPrefs.ConfirmPrompt.AUTO_CREATE, mission, difficulty});\n" +
                "((com.threerings.projectx.mission.client.t) ctx__.rq().aR(com.threerings.projectx.mission.client.t.class)).a(mission, difficulty, true, false, false, di);\n";
    }

    static String doScene(String sceneId) {
        return "com.threerings.projectx.util.A ctx__ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "ctx__.ru().au(" + sceneId + ");\n";
    }

    /**
     * @deprecated access denied
     */
    static String doZone(String zoneId, String sceneId) {
        return "com.threerings.projectx.util.A ctx___ = (com.threerings.projectx.util.A) this._ctx;\n" +
                "ctx___.xd().ad(" + zoneId + ", " + sceneId + ");\n";
    }

    public static void main(String[] args) {
    }
}
