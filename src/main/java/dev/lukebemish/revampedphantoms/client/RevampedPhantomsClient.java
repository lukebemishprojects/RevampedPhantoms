package dev.lukebemish.revampedphantoms.client;

import dev.lukebemish.revampedphantoms.Platform;
import dev.lukebemish.revampedphantoms.RevampedPhantoms;

public final class RevampedPhantomsClient {
	private RevampedPhantomsClient () {
	}

	public static void initialize(Platform platform, ClientPlatform clientPlatform) {
		clientPlatform.register(RevampedPhantoms.instance().shockwave::get, ShockwaveRenderer::new);
	}
}
