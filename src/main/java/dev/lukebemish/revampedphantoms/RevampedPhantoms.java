package dev.lukebemish.revampedphantoms;

import dev.lukebemish.revampedphantoms.client.ClientPlatform;
import dev.lukebemish.revampedphantoms.client.RevampedPhantomsClient;
import dev.lukebemish.revampedphantoms.effect.StunnedEffect;
import dev.lukebemish.revampedphantoms.entity.Shockwave;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

public class RevampedPhantoms {
	public static final String MOD_ID = "revamped_phantoms";
	private static final ResourceLocation ROOT_ID = new ResourceLocation(MOD_ID, MOD_ID);
	public static ResourceLocation id(String path) {
		return ROOT_ID.withPath(path);
	}


	public static final TagKey<EntityType<?>> PHANTOM_ATTACK_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, id("phantoms_do_not_attack"));

	private static @Nullable RevampedPhantoms INSTANCE;

	public final Supplier<MobEffect> stunned;
	public final Supplier<EntityType<Shockwave>> shockwave;

	public final Platform platform;

	private RevampedPhantoms(Platform platform) {
		this.stunned = platform.register(
			Registries.MOB_EFFECT,
			() -> new StunnedEffect(MobEffectCategory.HARMFUL, 0x795a94).addAttributeModifier(Attributes.MOVEMENT_SPEED, StunnedEffect.UUID, -0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
			id("stunned")
		);
		this.shockwave = platform.register(
			Registries.ENTITY_TYPE,
			() -> EntityType.Builder.<Shockwave>of(Shockwave::new, MobCategory.MISC)
				.sized(1.0f, 1.0f)
				.clientTrackingRange(4)
				.updateInterval(10)
				.build(id("shockwave").toString()),
			id("shockwave")
		);
		this.platform = platform;
	}

	public static synchronized void initialize(Platform platform) {
		if (INSTANCE != null) {
			throw new IllegalStateException("Already initialized");
		}
		INSTANCE = new RevampedPhantoms(platform);
		ClientPlatform clientPlatform;
		if ((clientPlatform = platform.client()) != null) {
			RevampedPhantomsClient.initialize(platform, clientPlatform);
		}
	}

	public static RevampedPhantoms instance() {
		if (INSTANCE == null) {
			throw new IllegalStateException("Not initialized");
		}
		return INSTANCE;
	}
}
