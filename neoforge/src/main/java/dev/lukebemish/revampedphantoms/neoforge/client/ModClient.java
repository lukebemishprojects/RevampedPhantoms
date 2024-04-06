package dev.lukebemish.revampedphantoms.neoforge.client;

import dev.lukebemish.revampedphantoms.client.ClientPlatform;
import dev.lukebemish.revampedphantoms.neoforge.ModEntrypoint;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import java.util.Objects;
import java.util.function.Supplier;

public final class ModClient implements ClientPlatform {
	public static final ModClient INSTANCE = new ModClient();

	private ModClient() {}

	@Override
	public <E extends Entity> void register(Supplier<EntityType<? extends E>> entityType, EntityRendererProvider<E> entityRendererFactory) {
		Objects.requireNonNull(ModEntrypoint.INSTANCE).modBus.addListener(EntityRenderersEvent.RegisterRenderers.class, event ->
			event.registerEntityRenderer(entityType.get(), entityRendererFactory)
		);
	}
}
