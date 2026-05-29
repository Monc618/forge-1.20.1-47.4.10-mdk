package com.Monc618.morecrossbow.item;

import com.Monc618.morecrossbow.MoreCrossbowMod;
import com.Monc618.morecrossbow.item.custom.*;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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

    // 1. 自定义一个“传说”稀有度，颜色代码 §6 是金色
    public static final Rarity LEGENDARY = Rarity.create("LEGENDARY", ChatFormatting.DARK_RED);

    public static final RegistryObject<Item> SONIC_BOOM =
            ITEMS.register("sonic_boom", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> WARDEN_CROSSBOW =
            ITEMS.register("warden_crossbow", () -> new WardenCrossbowItem((new Item.Properties()).durability(3465).rarity(ModItems.LEGENDARY)));
    // 注册金弩
    public static final RegistryObject<Item> COPPER_CROSSBOW =
            ITEMS.register("copper_crossbow", () -> new CopperCrossbowItem(new Item.Properties()));
    public static final RegistryObject<Item> IRON_CROSSBOW =
            ITEMS.register("iron_crossbow", () -> new IronCrossbowItem(new Item.Properties()));
    public static final RegistryObject<Item> GOLD_CROSSBOW =
            ITEMS.register("gold_crossbow", () -> new GoldCrossbowItem(new Item.Properties()));
    public static final RegistryObject<Item> EMERALD_CROSSBOW =
            ITEMS.register("emerald_crossbow", () -> new EmeraldCrossbowItem(new Item.Properties().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DIAMOND_CROSSBOW =
            ITEMS.register("diamond_crossbow", () -> new DiamondCrossbowItem(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> NETHERITE_CROSSBOW =
            ITEMS.register("netherite_crossbow", () -> new NetheriteCrossbowItem(new Item.Properties().rarity(Rarity.RARE)));

    /**
     * 注册方法，将物品注册到事件总线中
     * @param eventBus 事件总线，用于在适当的时机注册物品
     */
    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);

    }


}