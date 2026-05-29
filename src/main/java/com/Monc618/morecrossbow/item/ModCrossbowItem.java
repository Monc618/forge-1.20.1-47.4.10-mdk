package com.Monc618.morecrossbow.item;

import com.Monc618.morecrossbow.item.tier.CrossbowTier;
import com.Monc618.morecrossbow.item.tier.CrossbowTiers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.dedicated.Settings;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class ModCrossbowItem extends CrossbowItem {

    private final CrossbowTier tier;
    public ModCrossbowItem(CrossbowTier tier, Item.Properties properties) {
        super(properties.durability(tier.getDurability()));
        this.tier = tier;
    }
    public CrossbowTier getTier() {
        return tier;
    }
    /**
     * 获取考虑了快速装填附魔后的实际装填时间
     */

    public static int getChargeDuration(ItemStack stack, CrossbowTier tier) {
        int quickcharge = stack.getEnchantmentLevel(Enchantments.QUICK_CHARGE);
        // 从 Tier 中读取基础装填时间
        return Math.max(5, tier.getChargeTime() - 5 * quickcharge);
    }
    @Override
    public int getUseDuration(ItemStack pStack){
        return ModCrossbowItem.getChargeDuration(pStack,this.tier) + 3;
    }
    /**
     * 标准原版风格的核心逻辑：持续使用时每tick检测，拉满自动上膛
     */
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (!level.isClientSide && entity instanceof Player player) {
            int usedTicks = this.getUseDuration(stack) - remainingUseDuration;
            int totalUseDuration = this.getUseDuration(stack);
            if (usedTicks == 0) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, 0.65F, 1.0F);
                this.useOnRelease(stack);

            }

            // 【新增】2. 拉弦过半的声音 (当使用时间达到最大时间的 20% 时播放)
            // 只播放一次，所以判断 usedTicks == (totalUseDuration * 0.2) 向下取整
            if (usedTicks == totalUseDuration * 0.2) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 0.65F, 1.0F);
            }
        }
        if (!level.isClientSide && entity instanceof Player player) {
            int usedTicks = this.getUseDuration(stack) - remainingUseDuration;
            // 考虑了快速装填附魔后的实际装填时间
            int charge = ModCrossbowItem.getChargeDuration(stack,this.tier);

            // 当拉弦时间刚好达到要求，且弩还未上膛时，自动上膛
            if (usedTicks >= charge && !CrossbowItem.isCharged(stack)) {
                boolean loaded = this.tryLoadProjectiles(player, stack);
                if (loaded) {
                    CrossbowItem.setCharged(stack, true);
                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }
    /**
     * 标准原版逻辑：松开右键时无需额外处理上膛，因为 onUseTick 已经处理了
     */
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        // 如果不需要像铁弩那样特殊处理，这里留空即可
    }
    /**
     * 右键使用逻辑
     */
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // 如果已经上膛，执行发射
        if (CrossbowItem.isCharged(stack)) {
            if (!level.isClientSide) {
                // 从 Tier 中读取初速度
                float velocity = CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET)
                        ? 1.6F
                        : this.tier.getProjectileVelocity();

                CrossbowItem.performShooting(level, player, hand, stack, velocity, 1.0F);

                // 给射出的箭增加额外伤害（从 Tier 中读取）
                level.getEntitiesOfClass(net.minecraft.world.entity.projectile.AbstractArrow.class,
                        player.getBoundingBox().inflate(2.0D),
                        (arrow) -> arrow.getOwner() == player && arrow.tickCount <= 1
                ).forEach(arrow -> {
                    arrow.setBaseDamage(arrow.getBaseDamage() + this.tier.getBonusDamage());
                });
            }

            CrossbowItem.setCharged(stack, false);
            stack.removeTagKey("ChargedProjectiles");
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.consume(stack);
        } else {
            // 未上膛，开始拉弦
            boolean creative = player.getAbilities().instabuild;
            ItemStack ammo = player.getProjectile(stack);
            if (!creative && ammo.isEmpty()) {
                return InteractionResultHolder.fail(stack);
            } else {
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            }
        }
    }
    /**
     * 装填弹药逻辑
     */
    private boolean tryLoadProjectiles(Player player, ItemStack crossbow) {
        boolean creative = player.isCreative();
        ItemStack ammo = player.getProjectile(crossbow);

        if (ammo.isEmpty() && !creative) {
            return false;
        }

        ItemStack ammoType = ammo.isEmpty() ? new ItemStack(Items.ARROW) : ammo;
        int multishot = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, crossbow);
        int count = multishot > 0 ? 3 : 1;

        CompoundTag tag = crossbow.getOrCreateTag();
        ListTag list = new ListTag();

        for (int i = 0; i < count; ++i) {
            ItemStack one = ammoType.copy();
            one.setCount(1);
            CompoundTag oneTag = new CompoundTag();
            one.save(oneTag);
            list.add(oneTag);

            if (!creative && !ammo.isEmpty()) {
                ammo.shrink(1);
                if (ammo.isEmpty()) {
                    player.getInventory().removeItem(ammo);
                }
            }
        }

        tag.put("ChargedProjectiles", list);
        return true;
    }
    // 让自定义弩支持经验修补等附魔时，能返回 Tier 配置的附魔值
    @Override
    public int getEnchantmentValue() {
        return this.tier.getEnchantmentValue();
    }
    // 支持铁砧修复
    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return this.tier.getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
    }
}
