package com.Monc618.morecrossbow.unit;
    import com.Monc618.morecrossbow.item.ModItems;
    import com.google.gson.JsonObject;
    import com.mojang.serialization.Codec;
    import com.mojang.serialization.codecs.RecordCodecBuilder;
    import it.unimi.dsi.fastutil.objects.ObjectArrayList;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.storage.loot.LootContext;
    import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
    import net.minecraftforge.common.loot.IGlobalLootModifier;
    import net.minecraftforge.common.loot.LootModifier;
    public class AddCrossbowLootModifier extends LootModifier {
        public static final Codec<AddCrossbowLootModifier> CODEC = Codec.unit(AddCrossbowLootModifier::new);
        protected AddCrossbowLootModifier(LootItemCondition[] c) { super(c); }
        private AddCrossbowLootModifier() { super(new LootItemCondition[0]); }
        protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> g, LootContext c) {
            var r = c.getRandom();
            if (r.nextFloat() < 0.20F) g.add(new ItemStack(ModItems.COPPER_CROSSBOW.get()));
            if (r.nextFloat() < 0.15F) g.add(new ItemStack(ModItems.IRON_CROSSBOW.get()));
            if (r.nextFloat() < 0.10F) g.add(new ItemStack(ModItems.GOLD_CROSSBOW.get()));
            if (r.nextFloat() < 0.07F) g.add(new ItemStack(ModItems.EMERALD_CROSSBOW.get()));
            if (r.nextFloat() < 0.04F) g.add(new ItemStack(ModItems.DIAMOND_CROSSBOW.get()));
            return g;
        }
        public Codec<? extends IGlobalLootModifier> codec() { return CODEC; }
    }