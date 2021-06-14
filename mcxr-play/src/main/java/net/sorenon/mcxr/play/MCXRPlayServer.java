package net.sorenon.mcxr.play;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class MCXRPlayServer implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		Logger.getLogger("MCXR").warning("MCXR-Play should NOT be installed on servers");
	}
}
