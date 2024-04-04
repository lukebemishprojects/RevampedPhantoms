package dev.lukebemish.revampedphantoms.fabric;

import dev.lukebemish.revampedphantoms.ClientPlatform;
import dev.lukebemish.revampedphantoms.ModConfig;
import dev.lukebemish.revampedphantoms.Platform;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class ModEntrypoint implements ModInitializer, Platform {
    final ModConfig config;

    public ModEntrypoint() {
        this.config = ModConfig.handle(FabricLoader.getInstance().getConfigDir()).load();
    }

    @Override
    public void onInitialize() {
        RevampedPhantoms.initialize(this);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public <T> Supplier<T> register(ResourceKey<? extends Registry<? super T>> registryKey, Supplier<T> entry, ResourceLocation key) {
        Registry<? super T> registry = Objects.requireNonNull(BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey));
        T registered = Registry.register(
                registry,
            key,
            entry.get()
        );
        return () -> registered;
    }

    @Override
    public @Nullable ClientPlatform client() {
        return null;
    }

    @Override
    public ModConfig config() {
        return config;
    }
}
