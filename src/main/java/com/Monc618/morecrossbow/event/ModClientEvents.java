package com.Monc618.morecrossbow.event;

import com.Monc618.morecrossbow.item.ModItems;
import com.Monc618.morecrossbow.item.custom.IronCrossbowItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;


import java.util.UUID;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

// 客户端事件类（只在客户端运行）
@Mod.EventBusSubscriber(
        modid = "morecrossbow_mod",
        bus = Bus.FORGE,
        value = {Dist.CLIENT}
)
public class ModClientEvents {

    // FOV（视野）修改事件 —— 拉弓/弩时缩小视野
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onComputeFovModifierEvent(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();//创建一个Player类型的变量player，使用getPlayer（）获取触发这个事件的玩家名，保存在player里
        if (player == null) return;

        // 获取玩家移动速度属性
        AttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);//获取玩家移动速度属性
        AttributeModifier IronCrossbowModifier = speedAttribute != null ? speedAttribute.getModifier(IronCrossbowItem.AIM_SPEED_UUID) : null;//获取玩家移动速度属性中的自定义属性

        // 判断玩家是否正在使用各种弓/弩
        //boolean usingShortBow = player.isUsingItem() && player.getUseItem().is(ModItems.SHORT_BOW.get());
        //boolean usingLongBow = player.isUsingItem() && player.getUseItem().is(ModItems.LONG_BOW.get());
        boolean usingIronCrossbow = player.isUsingItem() && player.getUseItem().is(ModItems.IRON_CROSSBOW.get()) && !CrossbowItem.isCharged(player.getUseItem());

        boolean IronCrossbowActive = usingIronCrossbow || IronCrossbowModifier != null;

        // 如果正在使用自定义弓/弩 → 修改FOV
        if (IronCrossbowActive) {
           float fov = event.getFovModifier();
            fov = removeOnlyMySpeedFov(player, fov, IronCrossbowItem.AIM_SPEED_UUID);
            event.setNewFovModifier(fov);


            /*短弓：快速拉弓，轻微变焦
            if (usingShortBow) {
                fov = removeOnlyMySpeedFov(player, fov, ShortBowItem.AIM_FAST_UUID);
                int useTicks = player.getTicksUsingItem();
                float chargeTime = 15.0F;
                float zoom = 0.075F;
                float t = Mth.clamp((float) useTicks / chargeTime, 0.0F, 1.0F);
                float power = (t * t + 2.0F * t) / 3.0F;
                event.setNewFovModifier(fov * (1.0F - power * zoom));
            }

            // 长弓：慢拉弓，深度变焦
            else if (usingLongBow) {
                fov = removeOnlyMySpeedFov(player, fov, LongBowItem.AIM_SLOW_UUID);
                int useTicks = player.getTicksUsingItem();
                float chargeTime = 30.0F;
                float zoom = 0.3F;
                float t = Mth.clamp((float) useTicks / chargeTime, 0.0F, 1.0F);
                float power = (t * t + 2.0F * t) / 3.0F;
                event.setNewFovModifier(fov * (1.0F - power * zoom));
            }*/

            // 轻弩：不变焦，只修正速度带来的FOV影响


        }
    }

    /**
     * 移除自己添加的速度BUFF对FOV的影响，让视野不会乱跳
     */
    private static float removeOnlyMySpeedFov(Player player, float fov, UUID modifierUuid) {
        AttributeInstance speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (speedAttribute == null) return fov;

        AttributeModifier modifier = speedAttribute.getModifier(modifierUuid);
        if (modifier == null || modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
            return fov;
        }

        // 玩家行走速度
        float walkSpeed = player.getAbilities().getWalkingSpeed();
        if (walkSpeed <= 0.0F) return fov;

        // 计算：移除速度加成前后的真实移动速度
        double speedWithModifier = player.getAttributeValue(Attributes.MOVEMENT_SPEED);
        double factor = 1.0 + modifier.getAmount();
        double speedWithoutModifier = speedWithModifier / factor;

        // FOV 是根据移动速度计算的，这里修正回去
        double fovWith = (speedWithModifier / walkSpeed + 1.0) / 2.0;
        double fovWithout = (speedWithoutModifier / walkSpeed + 1.0) / 2.0;

        if (fovWith == 0) return fov;
        return (float) (fov * (fovWithout / fovWith));
    }
}
