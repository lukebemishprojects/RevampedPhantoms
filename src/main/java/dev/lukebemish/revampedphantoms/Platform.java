package dev.lukebemish.revampedphantoms;

import dev.lukebemish.revampedphantoms.client.ClientPlatform;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

public interface Platform {
	<R, T extends R> Supplier<T> register(ResourceKey<? extends Registry<R>> registry, Supplier<T> entry, ResourceLocation key);

	@Nullable ClientPlatform client();
	ModConfig config();

	default boolean onClient() {
		return client() != null;
	}
}
