package dev.lukebemish.revampedphantoms.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
	@Shadow
	private ClientLevel level;

	@WrapWithCondition(
		method = "handleSetEntityPassengersPacket(Lnet/minecraft/network/protocol/game/ClientboundSetPassengersPacket;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/gui/Gui;setOverlayMessage(Lnet/minecraft/network/chat/Component;Z)V"
		)
	)
	private boolean revamped_phantoms$wrapSetOverlay(Gui gui, Component component, boolean animate, ClientboundSetPassengersPacket packet) {
		Entity vehicle = this.level.getEntity(packet.getVehicle());
		return vehicle == null || vehicle.getType() != EntityType.PHANTOM;
	}

	@WrapWithCondition(
		method = "handleSetEntityPassengersPacket(Lnet/minecraft/network/protocol/game/ClientboundSetPassengersPacket;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/GameNarrator;sayNow(Lnet/minecraft/network/chat/Component;)V"
		)
	)
	private boolean revamped_phantoms$wrapSayNow(GameNarrator gameNarrator, Component component, ClientboundSetPassengersPacket packet) {
		Entity vehicle = this.level.getEntity(packet.getVehicle());
		return vehicle == null || vehicle.getType() != EntityType.PHANTOM;
	}
}
