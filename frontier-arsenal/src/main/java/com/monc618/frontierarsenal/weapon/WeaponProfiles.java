package com.monc618.frontierarsenal.weapon;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class WeaponProfiles extends SimpleJsonResourceReloadListener {
    public static final WeaponProfiles INSTANCE = new WeaponProfiles();
    private static volatile Map<ResourceLocation, WeaponProfile> loadedProfiles = Map.of();

    private WeaponProfiles() {
        super(new Gson(), "weapons");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager,
                         ProfilerFiller profiler) {
        Map<ResourceLocation, WeaponProfile> parsed = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }
            try {
                parsed.put(entry.getKey(), read(entry.getValue().getAsJsonObject()));
            } catch (IllegalArgumentException | JsonParseException ignored) {
                // A malformed data-pack entry is skipped; the item's built-in profile remains available.
            }
        }
        loadedProfiles = Map.copyOf(parsed);
    }

    private static WeaponProfile read(JsonObject json) {
        ResourceLocation ammunition = ResourceLocation.tryParse(GsonHelper.getAsString(json, "ammunition"));
        if (ammunition == null) {
            throw new JsonParseException("Invalid ammunition id");
        }
        return new WeaponProfile(
                ammunition,
                GsonHelper.getAsInt(json, "magazine_size"),
                GsonHelper.getAsFloat(json, "damage"),
                GsonHelper.getAsDouble(json, "range"),
                GsonHelper.getAsInt(json, "cooldown_ticks"),
                GsonHelper.getAsFloat(json, "recoil", 1.0F));
    }

    public Optional<WeaponProfile> get(ResourceLocation id) {
        return Optional.ofNullable(loadedProfiles.get(id));
    }
}
