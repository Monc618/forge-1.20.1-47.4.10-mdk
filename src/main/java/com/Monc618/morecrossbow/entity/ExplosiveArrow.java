package com.Monc618.morecrossbow.entity;
    import com.Monc618.morecrossbow.item.ModItems;
    import net.minecraft.core.particles.ParticleTypes;
    import net.minecraft.world.entity.EntityType;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    import net.minecraft.world.phys.BlockHitResult;
    import net.minecraft.world.phys.EntityHitResult;
    public class ExplosiveArrow extends ModAbstractArrow {
        private static final float EXPLOSION_STRENGTH = 1.5F;
        public ExplosiveArrow(EntityType<? extends ExplosiveArrow> type, Level level) { super(type, level); }
        public ExplosiveArrow(EntityType<? extends ExplosiveArrow> type, LivingEntity shooter, Level level) { super(type, shooter, level); }
        @Override protected ItemStack getPickupItem() { return new ItemStack(ModItems.EXPLOSIVE_ARROW.get()); }
        @Override protected void onHitEntity(EntityHitResult result) {
            super.onHitEntity(result);
            if (!this.level().isClientSide) { this.level().explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_STRENGTH, Level.ExplosionInteraction.BLOCK); this.discard(); }
        }
        @Override protected void onHitBlock(BlockHitResult result) {
            super.onHitBlock(result);
            if (!this.level().isClientSide) { this.level().explode(this, this.getX(), this.getY(), this.getZ(), EXPLOSION_STRENGTH, Level.ExplosionInteraction.BLOCK); this.discard(); }
        }
        @Override public void tick() {
            super.tick();
            if (this.level().isClientSide) { this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D); }
        }
    }