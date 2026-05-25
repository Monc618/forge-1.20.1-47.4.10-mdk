package com.Monc618.morecrossbow.item.custom;


import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;




/**
 * 铁质弩的物品类，继承自弩基类
 * 实现了弩的装填、瞄准和发射功能，并添加了瞄准时减缓移动速度的特性
 */
public class IronCrossbowItem extends CrossbowItem {
    // 瞄准速度减缓的数值常量
    public static final double AIM_SPEED_AMOUNT = (double) 3.5F;
    // 瞄准速度减缓的UUID标识符
    public static final UUID AIM_SPEED_UUID = UUID.fromString("d3f4a1b2-c5e6-4f7a-8b9c-0d1e2f3a4b5c");

    /**
     * 构造函数
     * @param pProperties 物品属性配置
     */
    public IronCrossbowItem(Item.Properties pProperties) {

        super(pProperties);
    }

    /**
     * 创建瞄准速度减缓的属性修改器
     * @return 返回一个属性修改器实例
     */
    private static AttributeModifier aimSlowModifier() {
        return new AttributeModifier(AIM_SPEED_UUID, "iron_crossbow_aim_speed", (double) 3.5F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    /**
     * 设置玩家的瞄准速度状态
     * @param player 玩家对象
     * @param enabled 是否启用瞄准速度减缓
     */
    private static void setAimingSpeed(Player player, boolean enabled) {
        // 获取玩家的“移动速度”属性
        AttributeInstance attr = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) {
            if (enabled) {
                // 启用：没buff就加上
                if (attr.getModifier(AIM_SPEED_UUID) == null) {
                    attr.removeModifier(AIM_SPEED_UUID);
                    attr.addTransientModifier(aimSlowModifier());
                }
            } else {
                // 禁用：有buff就移除
                if (attr.getModifier(AIM_SPEED_UUID) != null) {
                    attr.removeModifier(AIM_SPEED_UUID);
                }
            }
        }
    }

    public static void forceDisable(Player player) {
        setAimingSpeed(player, false);
    }

    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        super.onUseTick(level, entity, stack, remainingUseDuration);
        if (entity instanceof Player player) {
            // 判断：玩家是否正在**使用这把轻弩**
            boolean usingThis = player.isUsingItem() && player.getUseItem() == stack;
            setAimingSpeed(player, usingThis);
        }
    }

   /*public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        try {
            // 装填逻辑（后面讲）
        } finally {
            // 无论如何，最后一定关闭移速buff
            if (entity instanceof Player player) {
                setAimingSpeed(player, false);
            }
        }
    }*/
    // m_5551_ -> useOnRelease (松开使用键时调用)
    public void useOnRelease(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        try {
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

                return;
            }
        } finally {
            // 无论装填是否成功，只要松开右键，就取消瞄准减速效果
            if (entity instanceof Player player) {
                setAimingSpeed(player, false);
            }
        }
    }


    /*private int getMyChargeDuration(ItemStack stack) {
        int quickCharge = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        int base = 20; // 原版基础装填：25tick，这里改成20tick（更快）
        return Math.max(5, base - 5 * quickCharge);
    }*/

    private int getMyChargeDuration(ItemStack stack) {
        // m_44843_ -> getEnchantmentLevel (获取物品上的附魔等级)
        // f_44960_ -> Enchantments.QUICK_CHARGE (快速装填附魔)
        int quickcharge = stack.getEnchantmentLevel(Enchantments.QUICK_CHARGE);

        int base = 20; // 基础装填时间，20 ticks（1秒）

        // 计算最终时间：基础时间 - 5 * 快速装填等级，最低不低于 5 ticks
        return Math.max(5, base - 5 * quickcharge);
    }



    private boolean tryLoadProjectiles(Player player, ItemStack crossbow) {
        boolean creative = player.isCreative();
        ItemStack ammo = player.getProjectile(crossbow);

        // 没箭 + 不是创造 → 不能装
        if (ammo.isEmpty() && !creative) {
            return false;
        }

        ItemStack ammoType = ammo.isEmpty() ? new ItemStack(Items.ARROW) : ammo;
        int multishot = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, crossbow);
        int count = multishot > 0 ? 3 : 1; // 多重箭 = 一次装3发

        // 装填逻辑：把箭存入弩的NBT标签
        CompoundTag tag = crossbow.getOrCreateTag();
        ListTag list = new ListTag();

        for (int i = 0; i < count; ++i) {
            ItemStack one = ammoType.copy();
            one.setCount(1);
            CompoundTag oneTag = new CompoundTag();
            one.save(oneTag);
            list.add(oneTag);

            // 消耗箭矢（创造模式不消耗）
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

    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if (entity instanceof Player player) {
            // 如果没在使用这把弩 → 关闭速度buff
            boolean usingThis = player.isUsingItem() && player.getUseItem().getItem() instanceof IronCrossbowItem;
            if (!usingThis) {
                setAimingSpeed(player, false);
            }
        }
    }

    /*public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (CrossbowItem.isCharged(stack)) { // 弩已经装完箭
            if (!level.isClientSide) {
                // 发射箭矢（原版逻辑，速度1.6~2.0倍）
                float velocity = CrossbowItem.isFireArrow(stack) ? 1.6F : 2.0F;
                CrossbowItem.performShooting(level, player, hand, stack, velocity, 1.0F);
            }
            // 射完清空装填状态
            CrossbowItem.setCharged(stack, false);
            stack.removeTag("ChargedProjectiles");
            player.awardStat(Stats.ITEM_USED.get(this));
            return InteractionResultHolder.success(stack);
        } else {
            // 没装箭 → 检查有没有箭，然后开始装填
            boolean creative = player.isCreative();
            ItemStack ammo = player.getProjectile(stack);
            if (!creative && ammo.isEmpty()) {
                return InteractionResultHolder.fail(stack);
            } else {
                player.startUsingItem(hand); // 开始按住使用
                return InteractionResultHolder.success(stack);
            }
        }
    }*/
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // m_40932_ -> isCharged (判断弩是否已上膛)
        if (CrossbowItem.isCharged(stack)) {
            // 发射逻辑
            if (!level.isClientSide) { // f_46443_ -> isClientSide (是否在客户端)
                // m_40871_ -> containsProjectile (判断是否装填了烟花)
                // f_42688_ -> Items.FIREWORK_ROCKET
                float velocity = CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 2.0F;

                // m_40887_ -> performShooting (执行发射)
                CrossbowItem.performShooting(level, player, hand, stack, velocity, 1.0F);
            }

            // m_40884_ -> setCharged (设置上膛状态)
            CrossbowItem.setCharged(stack, false);
            // m_41749_ -> removeTagKey (移除NBT标签) - ⚠️ 注意：1.21+版本不要用这个，见下方说明
            stack.removeTagKey("ChargedProjectiles");
            // m_36246_ -> awardStat (给予玩家统计分数)
            // f_12982_ -> ITEM_USED
            player.awardStat(Stats.ITEM_USED.get(this));

            // m_19096_ -> succeed (交互成功)
            return InteractionResultHolder.success(stack);
        } else {
            // 装填逻辑
            // m_150110_ -> getAbilities (获取玩家能力)
            // f_35937_ -> instabuild (创造模式无限物品)
            boolean creative = player.getAbilities().instabuild;
            // m_6298_ -> getProjectile (获取可用弹药)
            ItemStack ammo = player.getProjectile(stack);

            // m_41619_ -> isEmpty (物品栈是否为空)
            if (!creative && ammo.isEmpty()) {
                // m_19100_ -> fail (交互失败)
                return InteractionResultHolder.fail(stack);
            } else {
                // m_6672_ -> startUsingItem (开始使用物品，即举起弩开始拉弦)
                player.startUsingItem(hand);
                return InteractionResultHolder.success(stack);
            }
        }
    }

}