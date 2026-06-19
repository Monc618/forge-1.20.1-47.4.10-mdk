package com.Monc618.morecrossbow.entity;
    import com.Monc618.morecrossbow.item.ModItems;
    import net.minecraft.core.BlockPos;
    import net.minecraft.core.particles.ParticleTypes;
    import net.minecraft.world.entity.EntityType;
    import net.minecraft.world.entity.LivingEntity;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.Level;
    import net.minecraft.world.level.block.Blocks;
    import net.minecraft.world.phys.BlockHitResult;
    import net.minecraft.world.phys.EntityHitResult;
    public class FireArrow extends ModAbstractArrow {
        private static final int FIRE_SECONDS = 5;
        public FireArrow(EntityType<? extends FireArrow> type, Level level) { super(type, level); }
        public FireArrow(EntityType<? extends FireArrow> type, LivingEntity shooter, Level level) { super(type, shooter, level); }
        @Override protected ItemStack getPickupItem() { return new ItemStack(ModItems.FIRE_ARROW.get()); }
        @Override protected void onHitEntity(EntityHitResult result) {
            super.onHitEntity(result);
            if (!this.level().isClientSide) { result.getEntity().setRemainingFireTicks(FIRE_SECONDS * 20); this.discard(); }
        }
        @Override protected void onHitBlock(BlockHitResult result) {
            super.onHitBlock(result);
            if (!this.level().isClientSide) {
                BlockPos bp = result.getBlockPos().relative(result.getDirection());
                if (this.level().isEmptyBlock(bp)) { this.level().setBlockAndUpdate(bp, Blocks.FIRE.defaultBlockState()); }
                this.discard();
            }
        }
        @Override public void tick() {
            super.tick();
            if (this.level().isClientSide) { this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D); }
        }
    }