package com.Monc618.morecrossbow.item.tier;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public enum CrossbowTiers implements CrossbowTier {
    // 格式：名称(耐久, 装填时间, 初速度, 额外伤害, 附魔值, 修复材料)
    COPPER(520,25,3.15F, 1.0F, 13, () -> Ingredient.of(Items.COPPER_INGOT)),
    IRON(880, 20, 3.0F, 1.5F, 15, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD(328, 16, 2.5F, 0.8F, 30, () -> Ingredient.of(Items.GOLD_INGOT)),
    EMERALD(1314,25,3.25F,1.2F,18,() -> Ingredient.of(Items.EMERALD)),
    DIAMOND(1561, 22, 3.5F, 2.0F, 22, () -> Ingredient.of(Items.DIAMOND)),
    NETHERITE(2031,20,4F, 3.0F, 25, () -> Ingredient.of(Items.NETHERITE_INGOT));



    private final int durability;
    private final int chargeTime;
    private final float velocity;
    private final float bonusDamage;
    private final int enchantmentValue;
    private final java.util.function.Supplier<Ingredient> repairIngredient;

    CrossbowTiers(int durability, int chargeTime, float velocity, float bonusDamage, int enchantmentValue, java.util.function.Supplier<Ingredient> repairIngredient) {
        this.durability = durability;
        this.chargeTime = chargeTime;
        this.velocity = velocity;
        this.bonusDamage = bonusDamage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    @Override public int getDurability() { return this.durability; }
    @Override public int getChargeTime() { return this.chargeTime; }
    @Override public float getProjectileVelocity() { return this.velocity; }
    @Override public float getBonusDamage() { return this.bonusDamage; }
    @Override public int getEnchantmentValue() { return this.enchantmentValue; }
    @Override public Ingredient getRepairIngredient() { return this.repairIngredient.get(); }
}

