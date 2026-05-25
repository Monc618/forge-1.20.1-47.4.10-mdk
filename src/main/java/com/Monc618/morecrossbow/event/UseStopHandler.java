package com.Monc618.morecrossbow.event;

import com.Monc618.morecrossbow.item.custom.IronCrossbowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(
        modid = "morecrossbow_mod",
        bus = Bus.FORGE
)
public class UseStopHandler {

    @SubscribeEvent
    public static void onStop(LivingEntityUseItemEvent.Stop event) {
        LivingEntity entity = event.getEntity();

        if (entity instanceof Player player) {
            // 只在服务器端执行
            if (!player.level().isClientSide()) {
                ItemStack stack = event.getItem();

                // 如果停止使用的是轻弩 → 强制关闭速度BUFF
                if (stack.getItem() instanceof IronCrossbowItem) {
                    IronCrossbowItem.forceDisable(player);
                }
            }
        }
    }
}
