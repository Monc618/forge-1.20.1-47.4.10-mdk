# Frontier Arsenal

An original firearms mod prototype for Minecraft Forge 1.20.1. The goal is to grow this into a polished, modular weapon experience with its own identity and assets.

## First playable slice

- Ranger Carbine with a 12-round internal magazine
- Scout Pistol with an 8-round internal magazine
- Rifle rounds crafted from one iron nugget and one gunpowder (four rounds per craft)
- Pistol rounds crafted from one copper nugget and one gunpowder (four rounds per craft)
- Right-click to fire; sneak and right-click to reload from the player's inventory
- Server-authoritative hitscan damage with block collision, a firing cooldown, light recoil, and weapon durability
- Per-weapon ammunition, magazine, damage, range, fire-rate, and recoil values loaded from reloadable data JSON
- Chinese and English item text

The current item icons and firing sound are temporary vanilla placeholders. No TaCZ source code, models, sounds, or textures are included. Weapon tuning files live in `data/frontierarsenal/weapons/` and can be overridden by a data pack.

## Compatibility

- Minecraft 1.20.1
- Forge 47.x
- Java 17

## Build

Run `gradlew build` on Windows or `./gradlew build` on macOS/Linux. The distributable JAR is created under `build/libs/`.

## Controls

| Input | Action |
| --- | --- |
| Right-click | Fire one round |
| Sneak + right-click | Load the magazine from rifle rounds in inventory |

## Direction

This prototype starts with a small, data-driven weapon roster. The next layers are custom weapon models and reload animations, dedicated audio, a weapon workbench, attachments, and external content-pack loading.
