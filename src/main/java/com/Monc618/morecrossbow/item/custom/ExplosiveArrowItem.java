package com.Monc618.morecrossbow.item.custom;
    import com.Monc618.morecrossbow.entity.ExplosiveArrow;
    import com.Monc618.morecrossbow.entity.ModEntityTypes;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.entity.projectile.AbstractArrow;
    import net.minecraft.world.item.ArrowItem;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    public class ExplosiveArrowItem extends ArrowItem {
        public ExplosiveArrowItem(Properties p) { super(p); }
        public AbstractArrow createArrow(Level l, ItemStack s, LivingEntity sh) { return new ExplosiveArrow(ModEntityTypes.EXPLOSIVE_ARROW.get(), sh, l); }
    }