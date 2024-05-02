package dev.lukebemish.revampedphantoms.effect;

import dev.lukebemish.revampedphantoms.utils.Accessors;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;

public class StunnedEffect extends MobEffect {
	public static final String UUID = "9b23836a-f1c3-3e21-98e4-7ae212063415";

	public StunnedEffect(MobEffectCategory mobEffectCategory, int i) {
		super(mobEffectCategory, i);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@SuppressWarnings("UnreachableCode")
	@Override
	public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
		if (livingEntity instanceof Player player) {
			//player.disableShield(true);
			if (player.tickCount % 4 != 1) {
				ItemCooldowns cooldowns = player.getCooldowns();
				for (var entry : Accessors.getCooldowns(cooldowns).entrySet()) {
					var inst = entry.getValue();
					int end = Accessors.getEndTime(inst) + 1;
					int start = Accessors.getStartTime(inst) + 1;
					var newInst = Accessors.cooldownInstance(start, end);
					Accessors.getCooldowns(cooldowns).put(entry.getKey(), newInst);
				}
				int useRemaining = player.getUseItemRemainingTicks();
				if (useRemaining != 0) {
					Accessors.setUseItemRemaining(player, useRemaining + 1);
				}
			}
		}

		return true;
	}
}
