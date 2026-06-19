package com.Monc618.morecrossbow.entity;
    import net.minecraft.world.entity.EntityType;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.entity.projectile.AbstractArrow;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    public abstract class ModAbstractArrow extends AbstractArrow {
        protected ModAbstractArrow(EntityType<? extends AbstractArrow> type, Level level) { super(type, level); }
        protected ModAbstractArrow(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level level) { super(type, shooter, level); }
        @Override protected abstract ItemStack getPickupItem();
    }