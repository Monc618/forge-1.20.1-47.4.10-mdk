package com.Monc618.morecrossbow.item.custom;
    import com.Monc618.morecrossbow.entity.FeatherlessArrow;
    import com.Monc618.morecrossbow.entity.ModEntityTypes;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.entity.projectile.AbstractArrow;
    import net.minecraft.world.item.ArrowItem;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    public class FeatherlessArrowItem extends ArrowItem {
        public FeatherlessArrowItem(Properties p) { super(p); }
        public AbstractArrow createArrow(Level l, ItemStack s, LivingEntity sh) { return new FeatherlessArrow(ModEntityTypes.FEATHERLESS_ARROW.get(), sh, l); }
    }