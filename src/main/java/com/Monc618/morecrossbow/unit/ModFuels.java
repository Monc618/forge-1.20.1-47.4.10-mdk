package com.Monc618.morecrossbow.unit;


import com.Monc618.morecrossbow.item.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(
        modid = "morecrossbow",
        bus = Bus.FORGE
)
public class ModFuels {




    public static final int RANGED_BURN_TIME = 300;

    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        if (item == ModItems.COPPER_CROSSBOW.get()) {
            event.setBurnTime(RANGED_BURN_TIME);
        } else if (item == ModItems.IRON_CROSSBOW.get()) {
            event.setBurnTime(RANGED_BURN_TIME);
        }else if (item == ModItems.GOLD_CROSSBOW.get()) {
            event.setBurnTime(RANGED_BURN_TIME);
        }else if (item == ModItems.EMERALD_CROSSBOW.get()) {
            event.setBurnTime(RANGED_BURN_TIME);
        }else if (item == ModItems.DIAMOND_CROSSBOW.get()) {
            event.setBurnTime(RANGED_BURN_TIME);
        }else if (item == ModItems.NETHERITE_CROSSBOW.get()) {
            event.setBurnTime(RANGED_BURN_TIME);
        }


    }

}

