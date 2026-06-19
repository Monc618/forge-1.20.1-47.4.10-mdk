package com.Monc618.morecrossbow.entity;
    import com.Monc618.morecrossbow.item.ModItems;
    import net.minecraft.world.entity.EntityType;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    public class FeatherlessArrow extends ModAbstractArrow {
        public FeatherlessArrow(EntityType<? extends FeatherlessArrow> type, Level level) { super(type, level); }
        public FeatherlessArrow(EntityType<? extends FeatherlessArrow> type, LivingEntity shooter, Level level) { super(type, shooter, level); }
        @Override protected ItemStack getPickupItem() { return new ItemStack(ModItems.FEATHERLESS_ARROW.get()); }
    }