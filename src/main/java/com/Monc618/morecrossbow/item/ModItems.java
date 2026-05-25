package com.Monc618.morecrossbow.item;

import com.Monc618.morecrossbow.MoreCrossbowMod;
import com.Monc618.morecrossbow.item.custom.IronCrossbowItem;
import com.Monc618.morecrossbow.unit.ModFuels;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * ModItems 类用于注册和管理模组中的物品
 * 该类使用 DeferredRegister 来注册物品，这是 Forge 中推荐的物品注册方式
 */
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MoreCrossbowMod.MOD_ID);

    public static final RegistryObject<Item> IRON_CROSSBOW =
            ITEMS.register("iron_crossbow", () -> new IronCrossbowItem((new Item.Properties()).durability(465)));
    public static final RegistryObject<Item> GOLD_CROSSBOW =
            ITEMS.register("gold_crossbow", () -> new IronCrossbowItem((new Item.Properties()).durability(465)));



    /**
     * 注册方法，将物品注册到事件总线中
     * @param eventBus 事件总线，用于在适当的时机注册物品
     */
    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);
    }

    /*
    public static final RegistryObject<Item> IRON_CROSSBOW;
     * 物品的延迟注册表
     * 用于在模组加载时注册所有物品
    public static final DeferredRegister<Item> ITEMS;
    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "morecrossbow");
        IRON_CROSSBOW = ITEMS.register("light_crossbow", () -> new IronCrossbowItem((new Item.Properties()).durability(465)));

    }**/
}