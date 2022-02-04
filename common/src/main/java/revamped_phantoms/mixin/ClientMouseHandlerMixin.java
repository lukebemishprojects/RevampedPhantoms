package revamped_phantoms.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.Options;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import revamped_phantoms.RevampedPhantoms;

@Mixin(MouseHandler.class)
public class ClientMouseHandlerMixin {

    @Redirect(method = {"turnPlayer"},
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;smoothCamera:Z", opcode = Opcodes.GETFIELD))
    private boolean revamped_phantoms_replaceSmoothCamera(Options options) {
        if (Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.hasEffect(RevampedPhantoms.STUNNED_EFFECT.get())) {
                return true;
            }
        }
        return options.smoothCamera;
    }
}
