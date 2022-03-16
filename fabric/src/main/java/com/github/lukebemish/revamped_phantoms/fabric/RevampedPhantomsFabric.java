package com.github.lukebemish.revamped_phantoms.fabric;

import com.github.lukebemish.revamped_phantoms.RevampedPhantoms;
import net.fabricmc.api.ModInitializer;

public class RevampedPhantomsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RevampedPhantoms.init();
    }
}
