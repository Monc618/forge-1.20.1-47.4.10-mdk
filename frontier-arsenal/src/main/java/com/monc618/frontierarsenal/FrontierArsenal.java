package com.monc618.frontierarsenal;

import com.monc618.frontierarsenal.item.ModCreativeTabs;
import com.monc618.frontierarsenal.item.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(FrontierArsenal.MOD_ID)
public final class FrontierArsenal {
    public static final String MOD_ID = "frontierarsenal";

    public FrontierArsenal() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modBus);
        ModCreativeTabs.register(modBus);
    }
}
