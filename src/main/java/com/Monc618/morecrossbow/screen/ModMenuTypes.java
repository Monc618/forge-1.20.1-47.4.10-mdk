package com.Monc618.morecrossbow.screen;
    import com.Monc618.morecrossbow.MoreCrossbowMod;
    import net.minecraft.core.registries.Registries;
    import net.minecraft.world.flag.FeatureFlags;
    import net.minecraft.world.inventory.MenuType;
    import net.minecraftforge.eventbus.api.IEventBus;
    import net.minecraftforge.registries.DeferredRegister;
    import net.minecraftforge.registries.RegistryObject;
    public class ModMenuTypes {
        public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MoreCrossbowMod.MOD_ID);
        public static final RegistryObject<MenuType<FletchingTableMenu>> FLETCHING_TABLE = MENUS.register("fletching_table", () -> new MenuType<>(FletchingTableMenu::new, FeatureFlags.VANILLA_SET));
        public static void register(IEventBus bus) { MENUS.register(bus); }
    }