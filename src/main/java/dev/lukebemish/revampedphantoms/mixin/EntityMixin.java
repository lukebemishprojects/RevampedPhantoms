package dev.lukebemish.revampedphantoms.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
	@ModifyReturnValue(
		method = "getDefaultPassengerAttachmentPoint(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/EntityAttachments;)Lnet/minecraft/world/phys/Vec3;",
		at = @At("RETURN")
	)
	private static Vec3 revamped_phantoms$defaultPassengerAttachmentPoint(Vec3 original, Entity vehicle, Entity passenger, EntityAttachments attachments) {
		if (vehicle.getType()== EntityType.PHANTOM) {
			var passengerAttachment = passenger.getVehicleAttachmentPoint(passenger);
			return original.subtract(0, passenger.getBbHeight() - passengerAttachment.y, 0);
		}
		return original;
	}
}
