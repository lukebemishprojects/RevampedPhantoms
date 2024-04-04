package dev.lukebemish.revampedphantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
    @ModifyExpressionValue(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;ZZ)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/ServerStatsCounter;getValue(Lnet/minecraft/stats/Stat;)Z"
        )
    )
    private int revamped_phantoms$makePhantomsSpawnMoreOften(int value) {
        return value + RevampedPhantoms.instance().platform.config().baseRestlessness();
    }
}
