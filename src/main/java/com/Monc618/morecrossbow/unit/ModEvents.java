package com.Monc618.morecrossbow.unit;
    import com.Monc618.morecrossbow.MoreCrossbowMod;
    import com.Monc618.morecrossbow.item.ModItems;
    import com.Monc618.morecrossbow.screen.FletchingTableMenu;
    import net.minecraft.resources.ResourceLocation;
    import net.minecraft.world.entity.EquipmentSlot;
    import net.minecraft.world.entity.monster.Pillager;
    import net.minecraft.world.entity.player.Inventory;
    import net.minecraft.world.entity.player.Player;
    import net.minecraft.world.item.Item;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.level.block.Blocks;
    import net.minecraft.world.level.storage.loot.LootPool;
    import net.minecraft.world.level.storage.loot.entries.LootItem;
    import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
    import net.minecraftforge.event.LootTableLoadEvent;
    import net.minecraftforge.event.entity.EntityJoinLevelEvent;
    import net.minecraftforge.event.entity.player.PlayerInteractEvent;
    import net.minecraftforge.eventbus.api.SubscribeEvent;
    import net.minecraftforge.fml.common.Mod;
    import java.util.Set;
    @Mod.EventBusSubscriber(modid = MoreCrossbowMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public class ModEvents {
        private static final Set<ResourceLocation> BASTION = Set.of(
            new ResourceLocation("minecraft:chests/bastion_treasure"),
            new ResourceLocation("minecraft:chests/bastion_other"),
            new ResourceLocation("minecraft:chests/bastion_bridge"),
            new ResourceLocation("minecraft:chests/bastion_hoglin_stable")
        );
        private static Item[] modCrossbows = null;
        private static Item[] getModCrossbows() {
            if (modCrossbows == null) {
                modCrossbows = new Item[]{ModItems.COPPER_CROSSBOW.get(), ModItems.IRON_CROSSBOW.get(),
                    ModItems.GOLD_CROSSBOW.get(), ModItems.EMERALD_CROSSBOW.get(), ModItems.DIAMOND_CROSSBOW.get()};
            }
            return modCrossbows;
        }
        @SubscribeEvent
        public static void onJoin(EntityJoinLevelEvent e) {
            if (e.getEntity() instanceof Pillager p && !e.getLevel().isClientSide()) {
                if (p.getRandom().nextFloat() < 0.30F) {
                    Item[] c = getModCrossbows();
                    p.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(c[p.getRandom().nextInt(c.length)]));
                }
            }
        }
        @SubscribeEvent
        public static void onClick(PlayerInteractEvent.RightClickBlock e) {
            if (e.getLevel().getBlockState(e.getPos()).is(Blocks.FLETCHING_TABLE)) {
                e.setCanceled(true);
                e.getEntity().openMenu(new net.minecraft.world.MenuProvider() {
                    public net.minecraft.network.chat.Component getDisplayName() { return net.minecraft.network.chat.Component.translatable("container.fletching_table"); }
                    public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, Inventory inv, Player p) { return new FletchingTableMenu(id, inv); }
                });
            }
        }
        @SubscribeEvent
        public static void onLoot(LootTableLoadEvent e) {
            if (BASTION.contains(e.getName())) {
                e.getTable().addPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModItems.WARDEN_UPGRADE_SMITHING_TEMPLATE.get()).setWeight(1))
                    .setRolls(ConstantValue.exactly(1.0F)).build());
            }
        }
    }