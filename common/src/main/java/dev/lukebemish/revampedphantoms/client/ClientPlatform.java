package dev.lukebemish.revampedphantoms.client;

import java.util.function.Supplier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public interface ClientPlatform {
	<E extends Entity> void register(Supplier<EntityType<? extends E>> entityType, EntityRendererProvider<E> entityRendererFactory);
}
