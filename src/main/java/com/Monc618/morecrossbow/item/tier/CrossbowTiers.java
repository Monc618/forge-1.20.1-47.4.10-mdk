package com.Monc618.morecrossbow.item.tier;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public enum CrossbowTiers implements CrossbowTier{
    // 格式：名称(基础装填时间, 耐久度, 伤害加成, 修复材料)
    // 格式：名称(耐久度, 额外伤害, 速度倍率, 基础装填时间, 附魔等级, 修复材料)
    // 速度倍率和附魔等级如果你暂时用不到，可以先写0，以后扩展再用

    IRON(350, 0.0f, 0, 20, 14, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD(50, 0.0f, 0, 15, 22, () -> Ingredient.of(Items.GOLD_INGOT)),    // 金弩拉得快，附魔好，但不耐用
    DIAMOND(1000, 1.0f, 0, 18, 15, () -> Ingredient.of(Items.DIAMOND)),
    NETHERITE(2000, 1.5f, 0, 16, 15, () -> Ingredient.of(Items.NETHERITE_INGOT));

    private final int durability;
    private final float attackdamagebonus;
    private final float speedMultiplier; // 预留字段：箭矢飞行速度倍率
    private final int chargeTime;      // 核心字段：基础装填时间
    private final int enchantmentValue;// 预留字段：附魔能力 (Enchantability)
    private final java.util.function.Supplier<Ingredient> repairIngredient;

    // 构造函数
    CrossbowTiers(int durability, float attackdamagebonus, float speedMultiplier, int chargeTime, int enchantmentValue, java.util.function.Supplier<Ingredient> repairIngredient) {
        this.durability = durability;
        this.attackdamagebonus = attackdamagebonus;
        this.speedMultiplier = speedMultiplier;
        this.chargeTime = chargeTime;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    // --- Getter 方法 ---

    public int getDurability() {
        return this.durability;
    }

    public float getAttackDamageBonus() {
        return this.attackdamagebonus;
    }

    public float getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    public int getChargeTime() {
        return this.chargeTime;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}


