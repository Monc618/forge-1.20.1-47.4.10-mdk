package com.Monc618.morecrossbow.screen;
    import com.Monc618.morecrossbow.item.ModItems;
    import net.minecraft.world.Container;
    import net.minecraft.world.SimpleContainer;
    import net.minecraft.world.entity.player.Inventory;
    import net.minecraft.world.entity.player.Player;
    import net.minecraft.world.inventory.AbstractContainerMenu;
    import net.minecraft.world.inventory.Slot;
    import net.minecraft.world.item.ItemStack;
    import net.minecraft.world.item.Items;
    import net.minecraft.world.level.Level;
    public class FletchingTableMenu extends AbstractContainerMenu {
        private static final int OUTPUT = 3;
        private static final int INV_START = 4;
        private static final int HOT_END = 40;
        private final SimpleContainer input = new SimpleContainer(3);
        private final SimpleContainer output = new SimpleContainer(1);
        public FletchingTableMenu(int id, Inventory pi) { this(id, pi, (Level)null); }
        public FletchingTableMenu(int id, Inventory pi, net.minecraft.network.FriendlyByteBuf buf) { this(id, pi, (Level)(buf!=null?pi.player.level():null)); }
        private FletchingTableMenu(int id, Inventory pi, Level l) {
            super(ModMenuTypes.FLETCHING_TABLE.get(), id);
            this.addSlot(new Slot(input, 0, 44, 17));
            this.addSlot(new Slot(input, 1, 44, 35));
            this.addSlot(new Slot(input, 2, 44, 53));
            this.addSlot(new Slot(output, 0, 116, 35) {
                public boolean mayPlace(ItemStack s) { return false; }
                public void onTake(Player p, ItemStack s) { super.onTake(p, s); consume(); }
            });
            for (int r=0;r<3;r++) for (int c=0;c<9;c++) this.addSlot(new Slot(pi, c+r*9+9, 8+c*18, 84+r*18));
            for (int c=0;c<9;c++) this.addSlot(new Slot(pi, c, 8+c*18, 142));
            input.addListener(cn -> update());
        }
        private void update() {
            ItemStack t=input.getItem(0), m=input.getItem(1), b=input.getItem(2), r=ItemStack.EMPTY;
            if (!t.isEmpty()&&!m.isEmpty()&&!b.isEmpty()) {
                if (t.is(Items.GUNPOWDER)&&m.is(Items.STICK)&&b.is(Items.FEATHER)) r=new ItemStack(ModItems.EXPLOSIVE_ARROW.get(),2);
                else if (t.is(Items.MAGMA_CREAM)&&m.is(Items.STICK)&&b.is(Items.FEATHER)) r=new ItemStack(ModItems.FIRE_ARROW.get(),2);
                else if (t.is(Items.IRON_NUGGET)&&m.is(Items.STICK)&&b.is(net.minecraft.tags.ItemTags.LEAVES)) r=new ItemStack(ModItems.FEATHERLESS_ARROW.get(),4);
            }
            output.setItem(0, r);
        }
        private void consume() { for(int i=0;i<3;i++) { ItemStack s=input.getItem(i); if(!s.isEmpty()) s.shrink(1); } update(); }
        public void removed(Player p) { super.removed(p); if(!p.level().isClientSide) clearContainer(p, input); }
        public void slotsChanged(Container c) { if(c==input) update(); super.slotsChanged(c); }
        public ItemStack quickMoveStack(Player p, int i) {
            ItemStack r=ItemStack.EMPTY; Slot sl=this.slots.get(i);
            if(sl!=null&&sl.hasItem()) {
                ItemStack si=sl.getItem(); r=si.copy();
                if(i==OUTPUT) { consume(); if(!this.moveItemStackTo(si,INV_START,HOT_END,true)) return ItemStack.EMPTY; sl.setByPlayer(ItemStack.EMPTY); }
                else if(i>=INV_START) { if(!this.moveItemStackTo(si,0,3,false)) return ItemStack.EMPTY; }
                else { if(!this.moveItemStackTo(si,INV_START,HOT_END,true)) return ItemStack.EMPTY; }
                if(si.isEmpty()) sl.setByPlayer(ItemStack.EMPTY); else sl.setChanged();
                if(si.getCount()==r.getCount()) return ItemStack.EMPTY;
                sl.onTake(p,si);
            }
            return r;
        }
        public boolean stillValid(Player p) { return true; }
    }