package dev.lukebemish.revampedphantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @SuppressWarnings({"DataFlowIssue"})
    @ModifyReturnValue(method = "getPassengerAttachmentPoint()Lorg/joml/Vector3f;", at = @At("RETURN"))
    private Vector3f revamped_phantoms$getRidingOffset(Vector3f old) {
        if (((Entity)(Object)this).getType() == EntityType.PHANTOM && RevampedPhantoms.instance().platform.config().phantomsGrabPrey()) {
            return old.add(0, -2, 0);
        }
        return old;
    }
}
