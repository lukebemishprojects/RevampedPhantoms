package revamped_phantoms.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import revamped_phantoms.RevampedPhantoms;
import revamped_phantoms.utils.SpawningUtils;

@Mixin(SpawnPlacements.class)
public class SpawnPlacementsMixin {

    @ModifyVariable(method="register",at=@At("HEAD"), argsOnly = true)
    private static SpawnPlacements.SpawnPredicate revamped_phantoms_modifyPhantomSpawning(SpawnPlacements.SpawnPredicate spawnPredicate, EntityType entityType, SpawnPlacements.Type placement, Heightmap.Types heightMap, SpawnPlacements.SpawnPredicate spawnPredicateUnused) {
        if (entityType == EntityType.PHANTOM && RevampedPhantoms.getConfig().isDoDaylightSpawns()) {
            return SpawningUtils::shouldSpawn;
        }
        return spawnPredicate;
    }
}
