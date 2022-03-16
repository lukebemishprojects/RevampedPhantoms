package com.github.lukebemish.revamped_phantoms.mixin;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.github.lukebemish.revamped_phantoms.RevampedPhantoms;

@Mixin(BiomeDefaultFeatures.class)
public abstract class BiomeDefaultFeaturesMixin {
    @Inject(method = "monsters", at = @At("TAIL"))
    private static void revamped_phantoms_monsters(MobSpawnSettings.Builder arg, int i, int j, int k, boolean bl, CallbackInfo ci) {
        if (RevampedPhantoms.getConfig().isDoDaylightSpawns()) {
            arg.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PHANTOM, 15, 1, 1));
        }
    }
}
