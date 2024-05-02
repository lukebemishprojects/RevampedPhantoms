package dev.lukebemish.revampedphantoms.fabric;

import dev.lukebemish.revampedphantoms.ModConfig;
import dev.lukebemish.revampedphantoms.Platform;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import dev.lukebemish.revampedphantoms.client.ClientPlatform;
import dev.lukebemish.revampedphantoms.fabric.client.ModClient;
import java.util.Objects;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

public class ModEntrypoint implements ModInitializer, Platform {
	private final ModConfig config;

	public ModEntrypoint() {
		this.config = ModConfig.handle(FabricLoader.getInstance().getConfigDir()).load();
	}

	@Override
	public void onInitialize() {
		RevampedPhantoms.initialize(this);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public <R, T extends R> Supplier<T> register(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> entry, ResourceLocation key) {
		Registry<R> registry = Objects.requireNonNull(BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey));
		T registered = Registry.register(
				registry,
			key,
			entry.get()
		);
		return () -> registered;
	}

	@Override
	public @Nullable ClientPlatform client() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			return ModClient.INSTANCE;
		}
		return null;
	}

	@Override
	public ModConfig config() {
		return config;
	}
}
