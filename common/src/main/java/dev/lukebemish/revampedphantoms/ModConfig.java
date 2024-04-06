package dev.lukebemish.revampedphantoms;

import com.mojang.serialization.Codec;
import dev.lukebemish.codecextras.ExtendedRecordCodecBuilder;
import dev.lukebemish.codecextras.config.ConfigType;
import dev.lukebemish.codecextras.config.GsonOpsIo;
import dev.lukebemish.codecextras.repair.FillMissingMapCodec;
import java.nio.file.Path;

public record ModConfig(
	boolean phantomsAttackAnimals,
	boolean phantomsAttackVillagers,
	float phantomElytraPursueModifier,
	boolean phantomsStunPrey,
	int ticksBetweenStunAttempts,
	int ticksStunDuration,
	boolean phantomsGrabPrey,
	int baseRestlessness
) {
	public static final ModConfig DEFAULT = new ModConfig(
		true,
		true,
		8.0f,
		true,
		20*10,
		20*6,
		true,
		84000
	);

	public static final Codec<ModConfig> CODEC = ExtendedRecordCodecBuilder
		.start(FillMissingMapCodec.fieldOf(Codec.BOOL, "phantoms_attack_animals", DEFAULT.phantomsAttackAnimals()), ModConfig::phantomsAttackAnimals)
		.field(FillMissingMapCodec.fieldOf(Codec.BOOL, "phantoms_attack_villagers", DEFAULT.phantomsAttackVillagers()), ModConfig::phantomsAttackVillagers)
		.field(FillMissingMapCodec.fieldOf(Codec.floatRange(1f, 32f), "phantom_elytra_pursue_modifier", DEFAULT.phantomElytraPursueModifier()), ModConfig::phantomElytraPursueModifier)
		.field(FillMissingMapCodec.fieldOf(Codec.BOOL, "phantoms_stun_prey", DEFAULT.phantomsStunPrey()), ModConfig::phantomsStunPrey)
		.field(FillMissingMapCodec.fieldOf(Codec.INT, "ticks_between_stun_attempts", DEFAULT.ticksBetweenStunAttempts()), ModConfig::ticksBetweenStunAttempts)
		.field(FillMissingMapCodec.fieldOf(Codec.INT, "ticks_stun_duration", DEFAULT.ticksStunDuration()), ModConfig::ticksStunDuration)
		.field(FillMissingMapCodec.fieldOf(Codec.BOOL, "phantoms_grab_prey", DEFAULT.phantomsGrabPrey()), ModConfig::phantomsGrabPrey)
		.field(FillMissingMapCodec.fieldOf(Codec.INT, "base_restlessness", DEFAULT.baseRestlessness()), ModConfig::baseRestlessness)
		.build(baseRestlessness -> phantomsGrabPrey -> ticksStunDuration -> ticksBetweenStunAttempts -> phantomsStunPrey -> phantomElytraPursueModifier -> phantomsAttackVillagers -> phantomsAttackAnimals ->
			new ModConfig(
				phantomsAttackAnimals,
				phantomsAttackVillagers,
				phantomElytraPursueModifier,
				phantomsStunPrey,
				ticksBetweenStunAttempts,
				ticksStunDuration,
				phantomsGrabPrey,
				baseRestlessness
			)
		);

	private static class Type extends ConfigType<ModConfig> {
		static final String CONFIG_NAME = RevampedPhantoms.MOD_ID+".json";

		@Override
		public Codec<ModConfig> codec() {
			return ModConfig.CODEC;
		}

		@Override
		public ModConfig defaultConfig() {
			return ModConfig.DEFAULT;
		}
	}

	public static ConfigType.ConfigHandle<ModConfig> handle(Path configDir) {
		Path configPath = configDir.resolve(Type.CONFIG_NAME);
		return new Type().handle(configPath, GsonOpsIo.INSTANCE);
	}
}
