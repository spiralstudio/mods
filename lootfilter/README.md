# LootFilter

Hide loot messages and pickup sprites.

## Usage

Please create a file named `loopfilter.yml` and put it in `code-mods`.

Configure pickups and messages that need to be hidden:

```yaml
pickup:
  type:
    # Filter out the following item types
    excluded:
      - Capsule
      - Vial
  name:
    # Keep the following item names
    included:
      - Remedy Capsule
      - Health Capsule
      - Super Health Capsule
      - Ultra Health Capsule
      - Ultra Poison Barrier
      - Ultra Shock Barrier
      - Ultra Freeze Barrier
      - Ultra Fire Barrier
      - Ultra Shock Vial
      - Ultra Fire Vial
      - Ultra Stun Vial
      - Ultra Curse Vial
      - Ultra Sleep Vial
      - Ultra Poison Vial
      - Ultra Freeze Vial
      - Luck Potion
      - Ocarina of Slime
message:
  type:
    # Filter out the following item types
    excluded:
      - Rarity
      - Material
      - Mineral
  name:
    # Keep the following item names
    included:
      - Radiant Fire Crystal
      - Flawed Orb of Alchemy
      - Simple Orb of Alchemy
      - Advanced Orb of Alchemy
      - Elite Orb of Alchemy
      - Eternal Orb of Alchemy
      - Spark of Life
      - Golden Slime Coin
      - Kat Tribe Fetish
      - Book of Dark Rituals
      - Obsidian Shard
      - Binding Essence
      - Perplexing Element
      - Lump of Coal
      - Spoiled Treat
      - Bottomless Stocking
      - Razor Ribbon
      - Indestructible Giftwrap
      - Ancient Shell
      - Fiendish Amu Glyph
      - Fiendish Nok Glyph
      - Fiendish Sol Glyph
      - Fiendish Tor Glyph
      - Fiendish Ur Glyph
      - Fiendish ID Card
      - Wicked Whisker
      - Punkin Sprout
```