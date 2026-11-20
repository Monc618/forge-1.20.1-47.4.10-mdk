package com.monc618.frontierarsenal.weapon;

import net.minecraft.resources.ResourceLocation;

public record WeaponProfile(ResourceLocation ammunition, int magazineSize, float damage,
                            double range, int cooldownTicks, float recoil) {
    public WeaponProfile {
        if (magazineSize < 1 || damage < 0.0F || range < 1.0D || cooldownTicks < 1 || recoil < 0.0F) {
            throw new IllegalArgumentException("Invalid firearm profile values");
        }
    }
}
