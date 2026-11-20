package com.monc618.frontierarsenal.event;

import com.monc618.frontierarsenal.FrontierArsenal;
import com.monc618.frontierarsenal.weapon.WeaponProfiles;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FrontierArsenal.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ModEvents {
    private ModEvents() {
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(WeaponProfiles.INSTANCE);
    }
}
