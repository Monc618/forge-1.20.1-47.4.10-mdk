package com.monc618.frontierarsenal.item;

import com.monc618.frontierarsenal.FrontierArsenal;
import com.monc618.frontierarsenal.weapon.WeaponProfile;
import com.monc618.frontierarsenal.weapon.WeaponProfiles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public final class FirearmItem extends Item {
    private final ResourceLocation profileId;
    private final WeaponProfile fallbackProfile;

    public FirearmItem(String profileId, WeaponProfile fallbackProfile, Properties properties) {
        super(properties);
        this.profileId = new ResourceLocation(FrontierArsenal.MOD_ID, profileId);
        this.fallbackProfile = fallbackProfile;
    }

    private WeaponProfile profile() {
        return WeaponProfiles.INSTANCE.get(profileId).orElse(fallbackProfile);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        WeaponProfile profile = profile();
        tooltip.add(Component.translatable("tooltip.frontierarsenal.magazine",
                getRounds(stack, profile), profile.magazineSize()));
        tooltip.add(Component.translatable("tooltip.frontierarsenal.controls"));
        tooltip.add(Component.translatable("tooltip.frontierarsenal.damage", profile.damage()));
        tooltip.add(Component.translatable("tooltip.frontierarsenal.range", (int) profile.range()));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack weapon = player.getItemInHand(hand);
        WeaponProfile profile = profile();

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                reload(weapon, player, profile);
            }
            return InteractionResultHolder.sidedSuccess(weapon, level.isClientSide);
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.sidedSuccess(weapon, level.isClientSide);
        }

        if (!level.isClientSide) {
            int rounds = getRounds(weapon, profile);
            if (rounds <= 0) {
                player.displayClientMessage(Component.translatable("message.frontierarsenal.empty"), true);
            } else {
                setRounds(weapon, rounds - 1, profile);
                fire(level, player, profile);
                weapon.hurtAndBreak(1, player, broken -> broken.broadcastBreakEvent(hand));
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }

        player.getCooldowns().addCooldown(this, profile.cooldownTicks());
        if (level.isClientSide) {
            player.setXRot(player.getXRot() - profile.recoil());
        }
        return InteractionResultHolder.sidedSuccess(weapon, level.isClientSide);
    }

    private static int getRounds(ItemStack weapon, WeaponProfile profile) {
        return Math.min(weapon.getOrCreateTag().getInt("MagazineRounds"), profile.magazineSize());
    }

    private static void setRounds(ItemStack weapon, int amount, WeaponProfile profile) {
        weapon.getOrCreateTag().putInt("MagazineRounds", Math.max(0, Math.min(profile.magazineSize(), amount)));
    }

    private static void reload(ItemStack weapon, Player player, WeaponProfile profile) {
        int current = getRounds(weapon, profile);
        int needed = profile.magazineSize() - current;
        if (needed <= 0) {
            player.displayClientMessage(Component.translatable("message.frontierarsenal.magazine_full"), true);
            return;
        }

        Item ammunitionItem = ForgeRegistries.ITEMS.getValue(profile.ammunition());
        if (ammunitionItem == null || ammunitionItem == Items.AIR) {
            player.displayClientMessage(Component.translatable("message.frontierarsenal.invalid_ammo"), true);
            return;
        }

        int loaded = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize() && loaded < needed; slot++) {
            ItemStack ammunition = player.getInventory().getItem(slot);
            if (!ammunition.is(ammunitionItem)) {
                continue;
            }
            int take = Math.min(needed - loaded, ammunition.getCount());
            ammunition.shrink(take);
            loaded += take;
        }

        if (loaded == 0) {
            player.displayClientMessage(Component.translatable("message.frontierarsenal.no_ammo"), true);
            return;
        }

        setRounds(weapon, current + loaded, profile);
        player.displayClientMessage(Component.translatable(
                "message.frontierarsenal.reloaded", current + loaded, profile.magazineSize()), true);
        player.getCooldowns().addCooldown(weapon.getItem(), 8);
    }

    private static void fire(Level level, Player player, WeaponProfile profile) {
        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle().normalize();
        Vec3 maxEnd = start.add(direction.scale(profile.range()));
        HitResult blockHit = level.clip(new ClipContext(
                start, maxEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
        Vec3 end = blockHit.getType() == HitResult.Type.MISS ? maxEnd : blockHit.getLocation();

        AABB searchBox = player.getBoundingBox().expandTowards(end.subtract(start)).inflate(1.0D);
        Optional<LivingEntity> target = level.getEntitiesOfClass(LivingEntity.class, searchBox,
                        entity -> entity != player && entity.isAlive())
                .stream()
                .filter(entity -> entity.getBoundingBox().inflate(0.25D).clip(start, end).isPresent())
                .min(Comparator.comparingDouble(entity -> entity.distanceToSqr(player)));

        target.ifPresent(entity -> entity.hurt(level.damageSources().playerAttack(player), profile.damage()));
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 0.8F, 1.35F);
        if (level instanceof ServerLevel serverLevel) {
            Vec3 muzzle = start.add(direction.scale(0.65D));
            serverLevel.sendParticles(ParticleTypes.SMOKE,
                    muzzle.x, muzzle.y, muzzle.z, 3, 0.025D, 0.025D, 0.025D, 0.005D);
            if (blockHit.getType() != HitResult.Type.MISS) {
                Vec3 impact = blockHit.getLocation();
                serverLevel.sendParticles(ParticleTypes.CRIT,
                        impact.x, impact.y, impact.z, 4, 0.03D, 0.03D, 0.03D, 0.02D);
            }
        }
    }
}
