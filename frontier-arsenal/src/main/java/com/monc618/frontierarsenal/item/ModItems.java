package com.monc618.frontierarsenal.item;

import com.monc618.frontierarsenal.FrontierArsenal;
import com.monc618.frontierarsenal.weapon.WeaponProfile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, FrontierArsenal.MOD_ID);

    public static final RegistryObject<Item> RIFLE_ROUND = ITEMS.register(
            "rifle_round", () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> RANGER_CARBINE = ITEMS.register(
            "ranger_carbine", () -> new FirearmItem("ranger_carbine",
                    new WeaponProfile(new ResourceLocation(FrontierArsenal.MOD_ID, "rifle_round"),
                            12, 6.0F, 48.0D, 5, 1.0F),
                    new Item.Properties().durability(1200)));

    public static final RegistryObject<Item> PISTOL_ROUND = ITEMS.register(
            "pistol_round", () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> SCOUT_PISTOL = ITEMS.register(
            "scout_pistol", () -> new FirearmItem("scout_pistol",
                    new WeaponProfile(new ResourceLocation(FrontierArsenal.MOD_ID, "pistol_round"),
                            8, 4.5F, 32.0D, 8, 1.4F),
                    new Item.Properties().durability(900)));

    private ModItems() {
    }

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
