package com.Monc618.morecrossbow.item.custom;


import java.util.List;

import com.Monc618.morecrossbow.item.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource; // 修复：缺失的导入
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;


/**
 * 铁质弩的物品类，继承自弩基类
 * 实现了弩的装填、瞄准和发射功能，并添加了瞄准时减缓移动速度的特性
 */
public class WardenCrossbowItem extends CrossbowItem {

    /**
     * 构造函数
     * @param pProperties 物品属性配置
     */
    public WardenCrossbowItem(Item.Properties pProperties) {

        super(pProperties);
    }




    /**
     * 当物品持续使用时的刻度处理方法
     * @param level 当前游戏世界/维度对象
     * @param entity 使用物品的实体对象
     * @param stack 正在被使用的物品堆栈
     * @param remainingUseDuration 剩余使用持续时间（刻度）
     */
    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        // 调用父类的onUseTick方法
        // 调用父类的 onUseTick 会导致原版自动上膛，所以不能调用 super
        // 我们需要自己补全声音逻辑

        if (!level.isClientSide && entity instanceof Player player) {
            int totalUseDuration = this.getUseDuration(stack);
            int usedTicks = totalUseDuration - remainingUseDuration;

            // 1. 刚开始拉弦的声音 (只在第一 tick 播放)
            if (usedTicks == 0) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.CROSSBOW_LOADING_START, SoundSource.PLAYERS, 0.65F, 1.0F);
            }

            // 2. 拉弦过半的声音 (当使用时间达到 20% 时播放一次)
            if (usedTicks == totalUseDuration * 0.2) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 0.65F, 1.0F);
            }
        }

    }


@Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {

            if (entity instanceof Player player) {
                // m_8105_ -> getUseDuration (获取物品最大使用时间)
                int usedTicks = this.getUseDuration(stack) - timeLeft;
                int charge = this.getMyChargeDuration(stack);

                // 如果拉弦时间不够，直接返回
                if (usedTicks < charge) {
                    return;
                }

                // m_40932_ -> isCharged (判断弩是否已经上膛)
                if (CrossbowItem.isCharged(stack)) {
                    return;
                }

                boolean loaded = this.tryLoadProjectiles(player, stack);
                if (loaded) {
                    // m_40884_ -> setCharged (设置弩为已上膛状态)
                    CrossbowItem.setCharged(stack, true);

                    // m_6263_ -> playSound (播放声音)
                    // m_20185_ -> getX (获取 X 坐标)
                    // m_20186_ -> getY (获取 Y 坐标)
                    // m_20189_ -> getZ (获取 Z 坐标)
                    // f_11841_ -> SoundEvents.CROSSBOW_LOADING_END (弩装填结束的咔哒声)
                    level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.CROSSBOW_LOADING_END, SoundSource.PLAYERS, 1.0F, 1.0F);
                }


            }

    }


    public static int getMyChargeDuration(ItemStack stack) {
        // m_44843_ -> getEnchantmentLevel (获取物品上的附魔等级)
        // f_44960_ -> Enchantments.QUICK_CHARGE (快速装填附魔)
        int quickcharge = stack.getEnchantmentLevel(Enchantments.QUICK_CHARGE);

        int base = 35; // 基础装填时间，20 ticks（1秒）

        // 计算最终时间：基础时间 - 5 * 快速装填等级，最低不低于 5 ticks
        return Math.max(5, base - 5 * quickcharge);
    }
    @Override
    public int getUseDuration(ItemStack pStack) {
        return getMyChargeDuration(pStack) + 3;
    }





    private boolean tryLoadProjectiles(Player player, ItemStack crossbow) {
        //boolean creative = player.isCreative();
        //ItemStack ammo = player.getProjectile(crossbow);

        // 没箭 + 不是创造 → 不能装
        //if (ammo.isEmpty() && !creative) {
            //return false;
        //}

        //ItemStack ammoType = ammo.isEmpty() ? new ItemStack(Items.ARROW) : ammo;
        int multishot = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, crossbow);
        int count = multishot > 0 ? 3 : 1; // 多重箭 = 一次装3发

        // 装填逻辑：把箭存入弩的NBT标签
        CompoundTag tag = crossbow.getOrCreateTag();
        ListTag list = new ListTag();

        for (int i = 0; i < count; ++i) {
            //ItemStack one = ammoType.copy();
            //one.setCount(1);
            //CompoundTag oneTag = new CompoundTag();
            //one.save(oneTag);
            //list.add(oneTag);
            // 我们使用原版的箭作为虚拟弹药存入NBT
            // 这样弩的渲染器才能正确显示"已上膛"的模型和拉弦动画
            ItemStack virtualAmmo = new ItemStack(ModItems.SONIC_BOOM.get());
            CompoundTag oneTag = new CompoundTag();
            virtualAmmo.save(oneTag);
            list.add(oneTag);

            /* 消耗箭矢（创造模式不消耗）
            if (!creative && !ammo.isEmpty()) {
                ammo.shrink(1);
                if (ammo.isEmpty()) {
                    player.getInventory().removeItem(ammo);
                }
            }*/
        }

        tag.put("ChargedProjectiles", list);
        return true;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // m_40932_ -> isCharged (判断弩是否已上膛)
        if (CrossbowItem.isCharged(stack)) {
            // 发射逻辑
            if (!level.isClientSide) {
                // 检查是否有多重射击附魔
                int multishot = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
                int count = multishot > 0 ? 3 : 1;

                float baseYaw = player.getYRot();
                float basePitch = player.getXRot();

                for (int i = 0; i < count; ++i) {


                    float yaw = baseYaw;
                    if (count == 3) {
                        if (i == 1) yaw -= 30.0F; // 向左偏
                        if (i == 2) yaw += 30.0F; // 向右偏

                    }
// 执行自定义声波攻击逻辑
                    performSonicBoom((ServerLevel) level, player, yaw, basePitch);

                }

                // 播放监守者发射声波的音效，更有代入感
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 1.0F, 1.0F);
                /*float velocity = CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.3F;
                CrossbowItem.performShooting(level, player, hand, stack, velocity, 1.0F);
                level.getEntitiesOfClass(net.minecraft.world.entity.projectile.AbstractArrow.class,
                        player.getBoundingBox().inflate(2.0D),
                        (arrow) -> arrow.getOwner() == player && arrow.tickCount <= 1
                ).forEach(arrow -> {
                    // 给基础伤害增加 2.0（即多加1颗心的伤害）
                    // 你可以修改这个数值，比如改成 4.0 就是多加2颗心
                    arrow.setBaseDamage(arrow.getBaseDamage() + 2.0);
                });*/
                CrossbowItem.setCharged(stack, false);
                stack.removeTagKey("ChargedProjectiles");
            }


            if (level.isClientSide) {

                player.awardStat(Stats.ITEM_USED.get(this));

            }
            return InteractionResultHolder.consume(stack);
        }else {
            /*boolean creative = player.getAbilities().instabuild;
            ItemStack ammo = player.getProjectile(stack);
            if (!creative && ammo.isEmpty()) {

                return InteractionResultHolder.fail(stack);
            } else {*/

                player.startUsingItem(hand);
                return InteractionResultHolder.consume(stack);
            //}
        }
    }
    /**
     * 核心逻辑：手动模拟原版监守者的声波攻击
     */
    private void performSonicBoom(ServerLevel level, Player player, float yaw, float pitch) {
        // 1. 计算声波发射的起始位置 (玩家眼部高度偏上一点)
        Vec3 startPos = player.position().add(0.0D, 1.6F, 0.0D);

        // 2. 根据传入的偏航角和俯仰角计算视线方向向量
        Vec3 lookDir = calculateViewVector(pitch, yaw).normalize();

        // 3. 声波的最大射程 (原版为15格水平，20格垂直，这里统一设为20格)
        double range = 20.0D;

        // 4. 绘制声波粒子轨迹 (直接照搬原版逻辑)
        for(int i = 1; i < Mth.floor(range) + 7; ++i) {
            Vec3 particlePos = startPos.add(lookDir.scale(i));
            level.sendParticles(ParticleTypes.SONIC_BOOM, particlePos.x, particlePos.y, particlePos.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
        // 5. 获取射线路径上的所有实体，并造成伤害和击退
        // 扩大碰撞箱范围进行检测 (沿视线方向延伸 range 距离，宽度1.5格)
        AABB searchBox = player.getBoundingBox().expandTowards(lookDir.scale(range)).inflate(1.5D);
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, searchBox, (target) -> {
            return target != player && target.isAlive(); // 不能打自己
        });

        DamageSource damageSource = level.damageSources().sonicBoom(player);

        for (LivingEntity target : targets) {
            // 精确判断实体是否在视线射线路径上 (距离视线向量小于 1.5 格)
            if (isLookingAtPoint(target, startPos, lookDir, range, 1.5D)) {
                // 造成 10 点伤害 (5颗心，与原版监守者一致)
                target.hurt(damageSource, 10.0F);

                // 计算击退 (与原版监守者逻辑完全一致，考虑了击退抗性)
                double knockbackResist = target.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
                double verticalKnockback = 0.5D * (1.0D - knockbackResist);
                double horizontalKnockback = 2.5D * (1.0D - knockbackResist);
                target.push(lookDir.x() * horizontalKnockback, lookDir.y() * verticalKnockback, lookDir.z() * horizontalKnockback);
            }
        }
    }

    /**
     * 辅助方法：计算给定偏航角和俯仰角的方向向量
     * (因为我们需要多重射击的偏移，不能直接用 player.getLookAngle())
     */
    private static Vec3 calculateViewVector(float pitch, float yaw) {
        float radPitch = pitch * ((float)Math.PI / 180F);
        float radYaw = -yaw * ((float)Math.PI / 180F);
        float cosYaw = Mth.cos(radYaw);
        float sinYaw = Mth.sin(radYaw);
        float cosPitch = Mth.cos(radPitch);
        float sinPitch = Mth.sin(radPitch);
        return new Vec3(sinYaw * cosPitch, -sinPitch, cosYaw * cosPitch);
    }

    /**
     * 辅助方法：判断实体的碰撞箱是否与一条射线相交
     */
    private boolean isLookingAtPoint(LivingEntity target, Vec3 startPos, Vec3 lookDir, double range, double margin) {
        Vec3 targetCenter = target.position().add(0, target.getBbHeight() / 2.0, 0);
        Vec3 toTarget = targetCenter.subtract(startPos);
        double projectionLength = toTarget.dot(lookDir); // 投影长度

        if (projectionLength < 0 || projectionLength > range) return false; // 在身后或超出射程

        // 计算实体中心到射线的垂直距离
        Vec3 closestPointOnLine = startPos.add(lookDir.scale(projectionLength));
        double distanceToLine = targetCenter.distanceTo(closestPointOnLine);

        // 如果距离小于 (实体半径 + margin)，视为击中
        double targetRadius = Math.max(target.getBbWidth() / 2.0, target.getBbHeight() / 2.0);
        return distanceToLine < targetRadius + margin;
    }
}