package com.Monc618.morecrossbow.unit;
    import com.Monc618.morecrossbow.MoreCrossbowMod;
    import com.mojang.serialization.Codec;
    import net.minecraftforge.common.loot.IGlobalLootModifier;
    import net.minecraftforge.eventbus.api.IEventBus;
    import net.minecraftforge.registries.DeferredRegister;
    import net.minecraftforge.registries.ForgeRegistries;
    import net.minecraftforge.registries.RegistryObject;
    public class ModLootModifiers {
        public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MoreCrossbowMod.MOD_ID);
        public static final RegistryObject<Codec<AddCrossbowLootModifier>> ADD_CROSSBOW = GLM.register("add_crossbow", () -> AddCrossbowLootModifier.CODEC);
        public static void register(IEventBus bus) { GLM.register(bus); }
    }