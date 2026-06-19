package com.Monc618.morecrossbow;

import com.Monc618.morecrossbow.item.ModCreativeModTabs;
import com.Monc618.morecrossbow.item.ModItems;
import com.Monc618.morecrossbow.unit.ModItemProperties;
import com.Monc618.morecrossbow.unit.ModLootModifiers;
import com.Monc618.morecrossbow.unit.TierCrossbowProperties;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MoreCrossbowMod.MOD_ID)
public class MoreCrossbowMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "morecrossbow_mod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // 1. Forge 底层实例化时需要调用无参构造函数（必须存在，否则崩溃）
    public MoreCrossbowMod() {
    }


    public MoreCrossbowMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModCreativeModTabs.register(modEventBus);
        com.Monc618.morecrossbow.entity.ModEntityTypes.register(modEventBus);
        com.Monc618.morecrossbow.screen.ModMenuTypes.register(modEventBus);
        ModLootModifiers.register(modEventBus);



        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);







    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }


    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(
            modid = MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.MOD,
            value = Dist.CLIENT
    )
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onRegisterRenderers(net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(com.Monc618.morecrossbow.entity.ModEntityTypes.EXPLOSIVE_ARROW.get(), com.Monc618.morecrossbow.entity.client.ModArrowRenderer::new);
            event.registerEntityRenderer(com.Monc618.morecrossbow.entity.ModEntityTypes.FIRE_ARROW.get(), com.Monc618.morecrossbow.entity.client.ModArrowRenderer::new);
            event.registerEntityRenderer(com.Monc618.morecrossbow.entity.ModEntityTypes.FEATHERLESS_ARROW.get(), com.Monc618.morecrossbow.entity.client.ModArrowRenderer::new);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

            ModItemProperties.addCustomItemProperties();
            TierCrossbowProperties.addCustomItemProperties();
            net.minecraft.client.gui.screens.MenuScreens.register(com.Monc618.morecrossbow.screen.ModMenuTypes.FLETCHING_TABLE.get(), com.Monc618.morecrossbow.screen.FletchingTableScreen::new);
        }
    }
}
