package com.Monc618.morecrossbow.unit;


import com.Monc618.morecrossbow.item.ModItems;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;





/**
 * 自定义物品属性类，用于注册和管理各种自定义物品的特殊属性和行为
 */
public class ModItemProperties {
    // 皮肤属性的资源位置标识
    public static final ResourceLocation SKIN = ResourceLocation.fromNamespaceAndPath("morecrossbow","skin");
    // 重新装填属性的资源位置标识
    private static final ResourceLocation RELOAD = ResourceLocation.fromNamespaceAndPath("morecrossbow","reload");
    // 开火属性的资源位置标识
    private static final ResourceLocation FIRE = ResourceLocation.fromNamespaceAndPath("morecrossbow","fire");
    // 模式属性的资源位置标识
    private static final ResourceLocation MODE = ResourceLocation.fromNamespaceAndPath("morecrossbow","mode");
    // 类型属性的资源位置标识
    private static final ResourceLocation TYPE = ResourceLocation.fromNamespaceAndPath("morecrossbow","type");
    // 已装填状态属性的资源位置标识
    private static final ResourceLocation LOADED = ResourceLocation.fromNamespaceAndPath("morecrossbow","loaded");

    /**
     * 注册所有自定义物品属性
     */
    public static void addCustomItemProperties() {
        // 注册长弓和短弓的拉弓动画属性
        //makeCustomBow(ModItems.LONG_BOW.get(), 30.0F);
        //makeCustomBow(ModItems.SHORT_BOW.get(), 15.0F);
        // 注册轻型弩的装填动画属性
        makeCustomCrossbow(ModItems.IRON_CROSSBOW.get(), 20.0F);
        // 注册连弩的各种自定义属性
        //makeCustomRep(ModItems.REPEATING_CROSSBOW.get());
        // 为弓添加自定义皮肤属性
        //addCustomSkins(ModItems.LONG_BOW.get());
        //ddCustomSkins(ModItems.SHORT_BOW.get());
    }

    /*
     * 为物品添加自定义皮肤属性
     * @param item 要添加皮肤属性的物品
     */
    /*private static void addCustomSkins(Item item) {
        ItemProperties.register(item, SKIN, (stack, level, entity, seed) -> {
            // 如果物品没有NBT标签，返回0
            if (!stack.hasTag()) {
                return 0.0F;
            } else {
                // 获取皮肤名称并判断是否为Vanilla皮肤
                String name = stack.getTag().getString("skin").trim();
                return name.equalsIgnoreCase("Vanilla") ? 1.0F : 0.0F;
            }
        });
    }*/

    /**
     * 为弓添加自定义属性
     * @param item 弓物品
     * @param ticks 拉弓动画持续时间（刻）
     */
    /*private static void makeCustomBow(Item item, float ticks) {
        // 注册拉弓进度属性
        ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                // 计算拉弓进度
                return entity.getUseItem() != stack ? 0.0F : (float)(stack.getUseDuration() - entity.getTicksUsingItem()) / ticks;
            }
        });
        // 注册是否正在拉弓属性
        ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, entity, i) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }*/

    /*
     * 为弩添加自定义属性
     * @param item 弩物品
     * @param ticks 装填动画持续时间（刻）
     */
    private static void makeCustomCrossbow(Item item, float ticks) {
        // 注册拉弦进度属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft","pull"), (ClampedItemPropertyFunction) (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                //float totalTicks = (float) item.getMyChargeDuration(stack);
                // 如果弩已上弦，返回0；否则返回装填进度
                return CrossbowItem.isCharged(stack) ? 0.0F : (float)entity.getTicksUsingItem() / ticks;
            }
        });
        // 注册是否正在拉弦属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft","pulling"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
        // 注册是否已上弦属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft","charged"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
        // 注册是否装载了烟花火箭属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft","firework"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack,Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
    }

    /**
     * 检查实体是否正在使用指定的物品
     * @param entity 实体
     * @param stack 物品堆
     * @return 如果实体正在使用该物品返回true，否则返回false
     */
    private static boolean usingThis(LivingEntity entity, ItemStack stack) {
        if (entity == null) {
            return false;
        } else if (!entity.isUsingItem()) {
            return false;
        } else {
            ItemStack using = entity.getUseItem();
            return ItemStack.matches(using, stack);
        }
    }

    /*
     * 为连弩添加自定义属性
     * @param item 连弩物品
     */
    /*private static void makeCustomRep(RepeatingCrossbowItem item) {
        // 注册装填进度属性
        ItemProperties.register(item, RELOAD, (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else if (RepeatingCrossbowItem.getMode(stack) != 2) {
                return 0.0F;
            } else if (!usingThis(entity, stack)) {
                return 0.0F;
            } else {
                // 计算装填进度
                int reloadTicks = RepeatingCrossbowItem.getReloadDurationLoad(stack);
                int usedTicks = stack.getUseDuration() - entity.getTicksUsingItem();
                return Mth.clamp((float) usedTicks / (float) reloadTicks, 0.0F, 1.0F);
            }
        // 注册开火动画属性
        });
        ItemProperties.register(item, FIRE, (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else if (RepeatingCrossbowItem.getMode(stack) != 1) {
                return 0.0F;
            } else if (!usingThis(entity, stack)) {
                return 0.0F;
                // 计算开火动画进度
            } else {
                int fireTicks = RepeatingCrossbowItem.getReloadDurationFire(stack);
                int anim = stack.getOrCreateTag().getInt("tm_fire_anim");
                return Mth.clamp((float) anim / (float) fireTicks, 0.0F, 1.0F);
            }
        // 注册模式属性
        });
        ItemProperties.register(item, MODE, (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else if (!usingThis(entity, stack)) {
                return 0.0F;
                // 根据弹药数量返回不同值
            } else {
                return RepeatingCrossbowItem.getMagCount(stack) > 0 ? 1.0F : 2.0F;
            }
        // 注册弹药类型属性
        });
        ItemProperties.register(item, TYPE, (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                ItemStack proj = RepeatingCrossbowItem.getNextAmmoType(stack);
                if (proj.isEmpty()) {
                    return 0.0F;
                    // 根据弹药类型返回不同值
                } else {
                    return proj.is(Items.FIREWORK_ROCKET) ? 1.0F : 2.0F;
                }
            }
        // 注册已装填状态属性
        });
        ItemProperties.register(item, LOADED, (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else if (usingThis(entity, stack)) {
                return 0.0F;
                // 根据弹药数量判断是否已装填
            } else {
                return RepeatingCrossbowItem.getMagCount(stack) > 0 ? 1.0F : 0.0F;
            }
        });
    }*/
}