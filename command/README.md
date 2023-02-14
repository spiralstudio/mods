# Command

It is the basis of the command-based mods.

**DO NOT RENAME IT!**

Keep its filename as "~command.jar", because it needs to be loaded at the end.

## Usage

Enter "/xhelp" to learn more about each command.

## Dev

```java
Method addCommand = Class.forName("com.spiralstudio.mod.command.Command")
    .getDeclaredMethod("addCommand", String.class, String.class);
addCommand.invoke(null, "hello", "System.out.println(\"Hello, World!\");");
```

`ProjectXContext`(`com.threerings.projectx.util.A`) can be referenced through `this._ctx`.

```java
com.threerings.projectx.util.A ctx = this._ctx;
```

```java
package com.threerings.projectx.util;
// com.threerings.projectx.util.ProjectXContext
public interface A extends a, m {
    // ProjectXApp getApp();
    ProjectXApp xb(); 
    // MuteDirector getMuteDirector();
    k xc(); 
    // ProjectXZoneDirector getZoneDirector();
    ex xd();
    // ZoneInstanceDirector getZoneGeneralDirector();
    fq xe();
    // ZoneInstanceDirector getZoneTradeDirector();
    fq xf();
    // NotificationDirector getNotificationDirector();
    n xg();
    // AdminDirector getAdminDirector();
    b xh();
    // GuildDirector getGuildDirector();
    L xi();
    // OfficerDirector getOfficerDirector();
    az xj();
    // PartyDirector getPartyDirector();
    R xk();
    // BattleSpriteDirector getBattleSpriteDirector(); ???
    dj xl();
    SteamDirector xm();
    eh xa();
    PlayerObject uk();
    ServerObject xn();
    long xo();
    String bu(String var1);
    String xlate(String var1, String var2);
    g xp();
    void b(DialogInfo var1);
}
```