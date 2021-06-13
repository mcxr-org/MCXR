package net.sorenon.minexraft;

import net.fabricmc.api.ModInitializer;

import java.util.logging.Logger;

public class MineXRaftServer implements ModInitializer {

	@Override
	public void onInitialize() {
		Logger.getLogger("MCXR").warning("MCXR-Play should NOT be installed on servers");
	}
}
