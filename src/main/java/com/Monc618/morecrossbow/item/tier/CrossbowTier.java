package com.Monc618.morecrossbow.item.tier;

import net.minecraft.world.item.crafting.Ingredient;

public interface CrossbowTier {
    int getDurability();          // 耐久度
    int getChargeTime();          // 基础装填时间
    float getProjectileVelocity(); // 箭矢初速度 (如 3.3F)
    float getBonusDamage();       // 额外增加的伤害 (如 2.0F)
    int getEnchantmentValue();    // 附魔能力
    Ingredient getRepairIngredient(); // 修复材料
}
