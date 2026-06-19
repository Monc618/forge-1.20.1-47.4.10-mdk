package com.Monc618.morecrossbow.item;

import com.Monc618.morecrossbow.MoreCrossbowMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.network.chat.Component;




public class ModCreativeModTabs {


    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreCrossbowMod.MOD_ID);



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> MORECROSSBOW_TAB =
            CREATIVE_MODE_TABS.register("morecrossbow_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.WARDEN_CROSSBOW.get()))
                    .title(Component.translatable("morecrossbow_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.COPPER_CROSSBOW.get());
                        pOutput.accept(ModItems.IRON_CROSSBOW.get());
                        pOutput.accept(ModItems.GOLD_CROSSBOW.get());
                        pOutput.accept(ModItems.EMERALD_CROSSBOW.get());
                        pOutput.accept(ModItems.DIAMOND_CROSSBOW.get());
                        pOutput.accept(ModItems.NETHERITE_CROSSBOW.get());
                        pOutput.accept(ModItems.WARDEN_CROSSBOW.get());
                        pOutput.accept(ModItems.WARDEN_UPGRADE_SMITHING_TEMPLATE.get());
                        pOutput.accept(ModItems.EXPLOSIVE_ARROW.get());
                        pOutput.accept(ModItems.FIRE_ARROW.get());
                        pOutput.accept(ModItems.FEATHERLESS_ARROW.get());


                    })
                    .build());
}