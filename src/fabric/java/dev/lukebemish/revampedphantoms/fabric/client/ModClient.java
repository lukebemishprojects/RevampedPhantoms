package dev.lukebemish.revampedphantoms.fabric.client;

import dev.lukebemish.revampedphantoms.client.ClientPlatform;
import java.util.function.Supplier;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public final class ModClient implements ClientPlatform {
	public static final ModClient INSTANCE = new ModClient();

	private ModClient() {}

	@Override
	public <E extends Entity> void register(Supplier<EntityType<? extends E>> entityType, EntityRendererProvider<E> entityRendererFactory) {
		EntityRendererRegistry.register(entityType.get(), entityRendererFactory);
	}
}
