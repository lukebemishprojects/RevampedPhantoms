package revamped_phantoms.fabric;

import revamped_phantoms.RevampedPhantoms;
import net.fabricmc.api.ModInitializer;

public class RevampedPhantomsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RevampedPhantoms.init();
    }
}
