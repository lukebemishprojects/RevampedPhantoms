package revamped_phantoms.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import revamped_phantoms.RevampedPhantoms;
import revamped_phantoms.RevampedPhantomsClient;

@Mod.EventBusSubscriber(modid = RevampedPhantoms.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RevampedPhantomsClientForge {
    public static void init(final FMLClientSetupEvent event) {
        RevampedPhantomsClient.init();
    }
}
