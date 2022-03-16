package com.github.lukebemish.revamped_phantoms;

import com.github.lukebemish.revamped_phantoms.mixin.ICooldownInstanceMixin;
import com.github.lukebemish.revamped_phantoms.mixin.IItemCooldownsMixin;
import com.github.lukebemish.revamped_phantoms.mixin.ILivingEntityMixin;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

import java.util.Map;

public class StunnedEffect extends MobEffect {
    public StunnedEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity instanceof Player player) {
            //player.disableShield(true);
            ItemCooldowns cooldowns = player.getCooldowns();
            if (player.tickCount % 4 != 1) {
                if (cooldowns instanceof IItemCooldownsMixin icgetter) {
                    for (Map.Entry<Item, ItemCooldowns.CooldownInstance> entry : icgetter.getCooldowns().entrySet()) {
                        ItemCooldowns.CooldownInstance inst = entry.getValue();
                        if (inst instanceof ICooldownInstanceMixin instGetter) {
                            int end = instGetter.getEndTime() + 1;
                            int start = instGetter.getStartTime() + 1;
                            ItemCooldowns.CooldownInstance newInst = ICooldownInstanceMixin.invokeNew(start, end);
                            icgetter.getCooldowns().put(entry.getKey(), newInst);
                        }
                    }
                }
            }
            if (player.tickCount % 4 != 1) {
                int useRemaining = ((ILivingEntityMixin)player).getUseItemRemaining();
                if (useRemaining != 0) {
                    ((ILivingEntityMixin) player).setUseItemRemaining(useRemaining + 1);
                }
            }
        }
    }
}
