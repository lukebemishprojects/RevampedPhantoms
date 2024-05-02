package dev.lukebemish.revampedphantoms.neoforge;

import dev.lukebemish.revampedphantoms.ModConfig;
import dev.lukebemish.revampedphantoms.Platform;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;
import dev.lukebemish.revampedphantoms.client.ClientPlatform;
import dev.lukebemish.revampedphantoms.neoforge.client.ModClient;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jspecify.annotations.Nullable;

@Mod(RevampedPhantoms.MOD_ID)
public class ModEntrypoint implements Platform {
	public static @Nullable ModEntrypoint INSTANCE;

	Map<ResourceKey<? extends Registry<?>>, Map<String, DeferredRegister<?>>> registers = new HashMap<>();
	public final IEventBus modBus;
	private final ModConfig config;


	public ModEntrypoint(IEventBus modBus) {
		this.modBus = modBus;
		this.config = ModConfig.handle(FMLPaths.CONFIGDIR.get()).load();
		initialize(this);
	}

	private synchronized static void initialize(ModEntrypoint modEntrypoint) {
		if (INSTANCE == null) {
			INSTANCE = modEntrypoint;
		} else {
			throw new IllegalStateException("Already initialized");
		}
		RevampedPhantoms.initialize(modEntrypoint);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <R, T extends R> Supplier<T> register(ResourceKey<? extends Registry<R>> registry, Supplier<T> entry, ResourceLocation key) {
		var register = (DeferredRegister<R>) registers.computeIfAbsent(
			registry,
			k -> new HashMap<>()
		).computeIfAbsent(
			key.toString(),
			k -> {
				var r = DeferredRegister.create(registry, key.getNamespace());
				r.register(modBus);
				return r;
			}
		);
		return register.register(key.getPath(), entry);
	}

	@Override
	public @Nullable ClientPlatform client() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			return ModClient.INSTANCE;
		}
		return null;
	}

	@Override
	public ModConfig config() {
		return config;
	}
}
