package net.sorenon.mcxr.play.rendering;

import net.sorenon.mcxr.play.input.ControllerPoses;
import org.lwjgl.openxr.XrFovf;

public class RenderPass {
    public static RenderPass VANILLA = new RenderPass();
    public static RenderPass GUI = new RenderPass();

    private RenderPass() {

    }

    public static class World extends RenderPass {
        public XrFovf fov = null;
        public int viewIndex = 0;
        public final ControllerPoses eyePoses = new ControllerPoses();

        private World() {

        }

        public static World create() {
            return new World();
        }
    }
}
