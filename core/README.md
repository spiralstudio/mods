# Core

It is the basis of all mods.

## Usage

Enter "/xhelp" to learn more about each command.

## Dev

### Registers

Add a class to guarantee its loading order.

```java
com.spiralstudio.mod.core.Registers.add(Test.class);
```

### Add one field

```java
com.spiralstudio.mod.core.Commands.addField("_x","int");
```

### Add fields

```java
com.spiralstudio.mod.core.Commands.addFields(Map.of("_x","int"));
```

### Add one command

```java
com.spiralstudio.mod.core.Commands.addCommand("hello","System.out.println(\"Hello, World!\");");
```

### Add commands

```java
com.spiralstudio.mod.core.Commands.addCommands(Map.of("hello","System.out.println(\"Hello, World!\");"));
```

### ProjectXContext

`ProjectXContext`(`com.threerings.projectx.util.A`) can be referenced through `this._ctx`.

```java
com.threerings.projectx.util.A ctx=this._ctx;
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