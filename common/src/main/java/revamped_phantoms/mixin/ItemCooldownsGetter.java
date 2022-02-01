package revamped_phantoms.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemCooldowns.class)
public interface ItemCooldownsGetter {
    @Accessor
    Map<Item, ItemCooldowns.CooldownInstance> getCooldowns();
}
