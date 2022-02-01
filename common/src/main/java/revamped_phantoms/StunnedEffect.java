package revamped_phantoms;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.jetbrains.annotations.Nullable;
import revamped_phantoms.mixin.CooldownInstanceGetter;
import revamped_phantoms.mixin.ItemCooldownsGetter;
import revamped_phantoms.mixin.LivingEntityInterMixin;

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
                if (cooldowns instanceof ItemCooldownsGetter icgetter) {
                    for (Map.Entry<Item, ItemCooldowns.CooldownInstance> entry : icgetter.getCooldowns().entrySet()) {
                        ItemCooldowns.CooldownInstance inst = entry.getValue();
                        if (inst instanceof CooldownInstanceGetter instGetter) {
                            int end = instGetter.getEndTime() + 1;
                            int start = instGetter.getStartTime() + 1;
                            ItemCooldowns.CooldownInstance newInst = CooldownInstanceGetter.invokeNew(start, end);
                            icgetter.getCooldowns().put(entry.getKey(), newInst);
                        }
                    }
                }
            }
            if (player.tickCount % 4 != 1) {
                int useRemaining = ((LivingEntityInterMixin)player).getUseItemRemaining();
                if (useRemaining != 0) {
                    ((LivingEntityInterMixin) player).setUseItemRemaining(useRemaining + 1);
                }
            }
        }
    }
}
