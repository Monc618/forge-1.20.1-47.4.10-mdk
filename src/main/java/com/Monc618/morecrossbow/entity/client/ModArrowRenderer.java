package com.Monc618.morecrossbow.entity.client;
    import com.Monc618.morecrossbow.MoreCrossbowMod;
    import com.Monc618.morecrossbow.entity.ExplosiveArrow;
    import com.Monc618.morecrossbow.entity.FireArrow;
    import com.Monc618.morecrossbow.entity.FeatherlessArrow;
    import com.Monc618.morecrossbow.entity.ModAbstractArrow;
    import net.minecraft.client.renderer.entity.ArrowRenderer;
    import net.minecraft.client.renderer.entity.EntityRendererProvider;
    import net.minecraft.resources.ResourceLocation;
    public class ModArrowRenderer extends ArrowRenderer<ModAbstractArrow> {
        private static final ResourceLocation VAN = new ResourceLocation("minecraft:textures/entity/projectiles/arrow.png");
        private static final ResourceLocation EXP = new ResourceLocation(MoreCrossbowMod.MOD_ID + ":textures/entity/projectiles/explosive_arrow.png");
        private static final ResourceLocation FIR = new ResourceLocation(MoreCrossbowMod.MOD_ID + ":textures/entity/projectiles/fire_arrow.png");
        private static final ResourceLocation FEA = new ResourceLocation(MoreCrossbowMod.MOD_ID + ":textures/entity/projectiles/featherless_arrow.png");
        public ModArrowRenderer(EntityRendererProvider.Context ctx) { super(ctx); }
        public ResourceLocation getTextureLocation(ModAbstractArrow e) {
            if (e instanceof ExplosiveArrow) return EXP;
            if (e instanceof FireArrow) return FIR;
            if (e instanceof FeatherlessArrow) return FEA;
            return VAN;
        }
    }