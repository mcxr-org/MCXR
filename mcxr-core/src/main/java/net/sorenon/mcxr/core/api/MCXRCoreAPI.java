package net.sorenon.mcxr.core.api;

import net.minecraft.world.entity.player.Player;
import net.sorenon.mcxr.core.accessor.PlayerExt;

public final class MCXRCoreAPI {
    private MCXRCoreAPI() {

    }

    public static boolean doesPlayerHaveMCXRActive(Player player) {
        return ((PlayerExt) player).isXR();
    }

    public static boolean doesPlayerHaveMCXRCoreInstalled(Player player) {
        throw new UnsupportedOperationException();
    }

    public static boolean doesPlayerHaveMCXRPlayInstalled(Player player) {
        throw new UnsupportedOperationException();
    }
}
