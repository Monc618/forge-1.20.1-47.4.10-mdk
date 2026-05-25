package com.Monc618.morecrossbow.item.tier;

import net.minecraft.world.item.crafting.Ingredient;

public interface CrossbowTier {
    int getDurability();

    float getAttackDamageBonus();

    float getSpeedMultiplier();

    int getChargeTime();

    int getEnchantmentValue();

    Ingredient getRepairIngredient();
}

