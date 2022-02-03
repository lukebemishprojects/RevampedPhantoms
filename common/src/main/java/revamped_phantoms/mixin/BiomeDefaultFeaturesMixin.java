package revamped_phantoms.mixin;

import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeDefaultFeatures.class)
public abstract class BiomeDefaultFeaturesMixin {
    @Inject(method = "monsters", at = @At("TAIL"))
    private static void revamped_phantoms_monsters(MobSpawnSettings.Builder arg, int i, int j, int k, boolean bl, CallbackInfo ci) {
        arg.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.PHANTOM, 10, 1, 1));
    }
}
