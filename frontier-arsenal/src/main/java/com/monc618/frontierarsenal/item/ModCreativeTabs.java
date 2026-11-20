package com.monc618.frontierarsenal.item;

import com.monc618.frontierarsenal.FrontierArsenal;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FrontierArsenal.MOD_ID);

    public static final RegistryObject<CreativeModeTab> FRONTIER_ARSENAL = TABS.register(
            "frontier_arsenal", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.frontierarsenal"))
                    .icon(() -> new ItemStack(ModItems.RANGER_CARBINE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.RANGER_CARBINE.get());
                        output.accept(ModItems.SCOUT_PISTOL.get());
                        output.accept(ModItems.RIFLE_ROUND.get());
                        output.accept(ModItems.PISTOL_ROUND.get());
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }
}
