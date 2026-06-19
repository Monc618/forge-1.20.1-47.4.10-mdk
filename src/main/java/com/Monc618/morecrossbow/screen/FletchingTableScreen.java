package com.Monc618.morecrossbow.screen;
    import com.Monc618.morecrossbow.MoreCrossbowMod;
    import net.minecraft.client.gui.GuiGraphics;
    import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
    import net.minecraft.network.chat.Component;
    import net.minecraft.resources.ResourceLocation;
    import net.minecraft.world.entity.player.Inventory;
    public class FletchingTableScreen extends AbstractContainerScreen<FletchingTableMenu> {
        private static final ResourceLocation TEX = new ResourceLocation(MoreCrossbowMod.MOD_ID + ":textures/gui/container/fletching_table.png");
        public FletchingTableScreen(FletchingTableMenu m, Inventory i, Component t) { super(m,i,t); }
        protected void renderBg(GuiGraphics g, float p, int mx, int my) {
            g.blit(TEX, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        }
        public void render(GuiGraphics g, int mx, int my, float p) { this.renderBackground(g); super.render(g,mx,my,p); this.renderTooltip(g,mx,my); }
        protected void renderLabels(GuiGraphics g, int mx, int my) { g.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false); }
    }