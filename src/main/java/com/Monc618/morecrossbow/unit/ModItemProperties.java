package com.Monc618.morecrossbow.unit;


import com.Monc618.morecrossbow.item.ModItems;

import com.Monc618.morecrossbow.item.custom.WardenCrossbowItem;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;

import java.util.function.Supplier;


/**
 * 自定义物品属性类，用于注册和管理各种自定义物品的特殊属性和行为
 */
public class ModItemProperties {


    /**
     * 注册所有自定义物品属性
     */
    public static void addCustomItemProperties() {


        makeCustomCrossbow(ModItems.WARDEN_CROSSBOW);


    }



    /*
     * 为弩添加自定义属性
     * @param item 弩物品
     * @param ticks 装填动画持续时间（刻）
     */
    private static void makeCustomCrossbow(Supplier<Item> itemSupplier) {
        // 注册拉弦进度属性
        ItemProperties.register(itemSupplier.get(), new ResourceLocation("minecraft:pull"), (ClampedItemPropertyFunction) (stack, level, entity, i) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                float totalTicks = (float) WardenCrossbowItem.getMyChargeDuration(stack);

                if(totalTicks<=0)return 1.0f;
                return CrossbowItem.isCharged(stack) ? 0.0F : (float)entity.getTicksUsingItem() / totalTicks;
            }
        });
        // 注册是否正在拉弦属性
        ItemProperties.register(itemSupplier.get(), new ResourceLocation("minecraft:pulling"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
        // 注册是否已上弦属性
        ItemProperties.register(itemSupplier.get(), new ResourceLocation("minecraft:charged"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
                CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
        // 注册是否装载了烟花火箭属性
        ItemProperties.register(itemSupplier.get(), new ResourceLocation("minecraft:firework"), (ClampedItemPropertyFunction) (stack, level, entity, i) ->
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


}