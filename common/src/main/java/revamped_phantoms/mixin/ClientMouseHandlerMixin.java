package revamped_phantoms.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import revamped_phantoms.RevampedPhantoms;

@Mixin(MouseHandler.class)
public class ClientMouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Redirect(method = {"turnPlayer"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD))
    private boolean revamped_phantoms_replaceSmoothCamera(Options options) {
        if (minecraft.player != null) {
            if (minecraft.player.hasEffect(RevampedPhantoms.STUNNED_EFFECT.get())) {
                return true;
            }
        }
        return options.smoothCamera;
    }
}
