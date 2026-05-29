package com.Monc618.morecrossbow.item.custom;

import com.Monc618.morecrossbow.item.ModCrossbowItem;
import com.Monc618.morecrossbow.item.tier.CrossbowTiers;
import net.minecraft.world.item.Item;

public class GoldCrossbowItem extends ModCrossbowItem {
    public GoldCrossbowItem(Item.Properties pProperties) {
        // 将 GOLD tier 传入父类，一切逻辑父类都处理好了
        super(CrossbowTiers.GOLD, pProperties);
    }
}
