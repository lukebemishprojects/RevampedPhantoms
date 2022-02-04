package revamped_phantoms;

import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import revamped_phantoms.client.ShockwaveRenderer;

@Environment(value= EnvType.CLIENT)
public class RevampedPhantomsClient {
    public static void init() {
        EntityRendererRegistry.register(RevampedPhantoms.SHOCKWAVE::get, ShockwaveRenderer::new);
    }
}
