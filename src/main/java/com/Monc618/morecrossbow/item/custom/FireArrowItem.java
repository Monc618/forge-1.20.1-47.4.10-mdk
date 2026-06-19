package com.Monc618.morecrossbow.item.custom;
    import com.Monc618.morecrossbow.entity.FireArrow;
    import com.Monc618.morecrossbow.entity.ModEntityTypes;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.entity.projectile.AbstractArrow;
    import net.minecraft.world.item.ArrowItem;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    public class FireArrowItem extends ArrowItem {
        public FireArrowItem(Properties p) { super(p); }
        public AbstractArrow createArrow(Level l, ItemStack s, LivingEntity sh) { return new FireArrow(ModEntityTypes.FIRE_ARROW.get(), sh, l); }
    }