package io.github.TillDawn.Controllers;

import io.github.TillDawn.Views.HintMenuView;

public class HintMenuController {
    private HintMenuView view;

    private final String[] heroHints = {
        "Hero 1: Strong melee attacker with high defense.",
        "Hero 2: Skilled archer with long range attacks.",
        "Hero 3: Mage with powerful area spells."
    };

    private final String[] activeKeys = {
        "W - Move Up",
        "A - Move Left",
        "S - Move Down",
        "D - Move Right",
        "Space - Jump"
    };

    private final String[] cheatCodes = {
        "GODMODE - Invincibility",
        "INFINITE_AMMO - Unlimited ammo",
        "NOCOLLISION - Walk through walls"
    };

    private final String[] abilityDescriptions = {
        "Fireball: Throws a fireball that explodes on impact.",
        "Heal: Restores health over time.",
        "Stealth: Become invisible to enemies for 10 seconds."
    };

    public void setView(HintMenuView view) {
        this.view = view;
    }

    public void update() {
    }

    public String[] getHeroHints() {
        return heroHints;
    }

    public String[] getActiveKeys() {
        return activeKeys;
    }

    public String[] getCheatCodes() {
        return cheatCodes;
    }

    public String[] getAbilityDescriptions() {
        return abilityDescriptions;
    }
}
