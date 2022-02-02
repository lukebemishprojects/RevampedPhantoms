package revamped_phantoms.mixin;

import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemCooldowns.CooldownInstance.class)
public interface ICooldownInstanceMixin {
    @Accessor
    int getStartTime();
    @Accessor
    int getEndTime();
    @Invoker("<init>")
    static ItemCooldowns.CooldownInstance invokeNew(int i, int j) {
        throw new AssertionError("Untransformed accessor");
    }
}
