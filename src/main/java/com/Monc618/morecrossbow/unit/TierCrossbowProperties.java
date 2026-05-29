package com.Monc618.morecrossbow.unit;

import com.Monc618.morecrossbow.item.ModCrossbowItem;
import com.Monc618.morecrossbow.item.ModItems;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.ToIntFunction;

public class TierCrossbowProperties {


    public static void addCustomItemProperties() {

        // 注册金弩：动态从 ModCrossbowItem 和 CrossbowTier 中读取装填时间
        registerTierCrossbow(ModItems.GOLD_CROSSBOW.get());
        registerTierCrossbow(ModItems.COPPER_CROSSBOW.get());
        registerTierCrossbow(ModItems.DIAMOND_CROSSBOW.get());
        registerTierCrossbow(ModItems.EMERALD_CROSSBOW.get());
        registerTierCrossbow(ModItems.IRON_CROSSBOW.get());
        registerTierCrossbow(ModItems.NETHERITE_CROSSBOW.get());



        // 如果以后有钻石弩、下界合金弩，只要它们也是 ModCrossbowItem 的子类，都可以这样一行注册：
        // TierCrossbowProperties.register(ModItems.DIAMOND_CROSSBOW.get(), stack -> ModCrossbowItem.getChargeDuration(stack, ((ModCrossbowItem)stack.getItem()).getTier()));
    }

    /**
     * 专门为铁弩添加自定义属性
     */
    /**
     * 通用方法：为所有基于 ModCrossbowItem 的弩注册属性
     */
    private static void registerTierCrossbow(Item item) {
        // 传入获取装填时间的方法：从物品的 Tier 配置中自动获取
        register(item, stack -> {
            ModCrossbowItem crossbow = (ModCrossbowItem) stack.getItem();
            return ModCrossbowItem.getChargeDuration(stack, crossbow.getTier());
        });
    }

    /**
     * 底层注册方法
     */
    private static void register(Item item, ToIntFunction<ItemStack> chargeDurationFetcher) {
        // 注册拉弦进度属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft", "pull"), (ClampedItemPropertyFunction) (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            }


                else {
                    float totalTicks = (float) ModCrossbowItem.getChargeDuration(stack);

                    if(totalTicks<=0)return 1.0f;
                    return CrossbowItem.isCharged(stack) ? 0.0F : (float)entity.getTicksUsingItem() / totalTicks;
                }
            });

        // 注册是否正在拉弦属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft", "pulling"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

        // 注册是否已上弦属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft", "charged"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);

        // 注册是否装载了烟花火箭属性
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("minecraft", "firework"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
    }
}