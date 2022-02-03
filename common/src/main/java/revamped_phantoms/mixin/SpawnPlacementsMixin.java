package revamped_phantoms.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import revamped_phantoms.Utils;

@Mixin(SpawnPlacements.class)
public class SpawnPlacementsMixin {

    @Unique
    private static EntityType revamped_phantoms_lastEntity;

    @ModifyVariable(method="register", at=@At("HEAD"), argsOnly=true)
    private static EntityType revamped_phantoms_modifyPhantomSpawning(EntityType entityType) {
        revamped_phantoms_lastEntity = entityType;
        return entityType;
    }

    @ModifyVariable(method="register", at=@At("HEAD"), argsOnly=true)
    private static SpawnPlacements.SpawnPredicate revamped_phantoms_modifyPhantomSpawning(SpawnPlacements.SpawnPredicate spawnPredicate) {
        if (revamped_phantoms_lastEntity == EntityType.PHANTOM) {
            return Utils::shouldSpawn;
        }
        return spawnPredicate;
    }
}
