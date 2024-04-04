package dev.lukebemish.revampedphantoms;

import dev.lukebemish.codecextras.config.ConfigType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public interface Platform {
    <T> Supplier<T> register(ResourceKey<? extends Registry<? super T>> registry, Supplier<T> entry, ResourceLocation key);

    @Nullable ClientPlatform client();
    ModConfig config();

    default boolean onClient() {
        return client() != null;
    }
}
