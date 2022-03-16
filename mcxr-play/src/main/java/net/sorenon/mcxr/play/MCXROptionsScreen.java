package net.sorenon.mcxr.play;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.sorenon.mcxr.play.input.XrInput;
import net.sorenon.mcxr.play.openxr.OpenXRInstance;
import net.sorenon.mcxr.play.openxr.OpenXRState;
import net.sorenon.mcxr.play.openxr.OpenXRSystem;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openxr.XR10;

import java.util.List;

public class MCXROptionsScreen extends Screen {

    @Nullable
    private final Screen previous;

    public MCXROptionsScreen(@Nullable Screen previous) {
        super(new TranslatableComponent("mcxr.options.title"));
        this.previous = previous;
    }

    @Override
    protected void init() {
/*        this.addRenderableWidget(new Button(
                this.width / 2 - 155,
                this.height / 6 - 12,
                150,
                20,
                new TranslatableComponent("mcxr.menu.reload"),
                button -> MCXRPlayClient.OPEN_XR_STATE.tryInitialize()));*/
/*        this.addRenderableWidget(new Button(
                this.width / 2 + 5,
                this.height / 6 - 12,
                150,
                20,
                MCXRPlayClient.xrDisabled ? new TranslatableComponent("mcxr.options.enable") : new TranslatableComponent("mcxr.options.disable"),
                button -> {
                    MCXRPlayClient.xrDisabled = !MCXRPlayClient.xrDisabled;
                    button.setMessage(MCXRPlayClient.xrDisabled ? new TranslatableComponent("mcxr.options.enable") : new TranslatableComponent("mcxr.options.disable"));
                }));*/
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 6 + 168, 200, 20, CommonComponents.GUI_DONE, button -> this.minecraft.setScreen(this.previous)));
        this.addRenderableWidget(new Button(
                this.width / 2 - 155,
                this.height / 6 + 54,
                150,
                20,
                new TranslatableComponent("mcxr.options.walk_direction", MCXRPlayClient.walkDirection.toComponent()),
                button -> {
                    MCXRPlayClient.walkDirection = MCXRPlayClient.walkDirection.iterate();
                    button.setMessage(new TranslatableComponent("mcxr.options.walk_direction", MCXRPlayClient.walkDirection.toComponent()));
                }));
        this.addRenderableWidget(new Button(
                this.width / 2 - 155,
                this.height / 6 + 54 + 24,
                150,
                20,
                new TranslatableComponent("mcxr.options.swim_direction", MCXRPlayClient.swimDirection.toComponent()),
                button -> {
                    MCXRPlayClient.swimDirection = MCXRPlayClient.swimDirection.iterate();
                    button.setMessage(new TranslatableComponent("mcxr.options.swim_direction", MCXRPlayClient.swimDirection.toComponent()));
                }));
        this.addRenderableWidget(new Button(
                this.width / 2 - 155,
                this.height / 6 + 54 + 24 * 2,
                150,
                20,
                new TranslatableComponent("mcxr.options.fly_direction", MCXRPlayClient.flyDirection.toComponent()),
                button -> {
                    MCXRPlayClient.flyDirection = MCXRPlayClient.flyDirection.iterate();
                    button.setMessage(new TranslatableComponent("mcxr.options.fly_direction", MCXRPlayClient.flyDirection.toComponent()));
                }));

        assert this.minecraft != null;
        this.addRenderableWidget(Option.MAIN_HAND.createButton(this.minecraft.options, this.width / 2 - 155 + 160, this.height / 6 + 54, 150));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, delta);

        drawCenteredString(poseStack, this.font, this.title, this.width / 2, 15, 16777215);

        int y = this.height / 6 - 12 + 12;
        int x = this.width / 2 - 155;

        MCXROptionsScreen.renderStatus(this, this.font, poseStack, mouseX, mouseY, x, y, 0, 60);
    }

    public static void renderStatus(Screen screen,
                                    Font font,
                                    PoseStack poseStack,
                                    int mouseX,
                                    int mouseY,
                                    int x,
                                    int y,
                                    int fade,
                                    int wrapLength) {
        OpenXRState OPEN_XR = MCXRPlayClient.OPEN_XR_STATE;

        if (OPEN_XR.instance != null) {
            OpenXRInstance instance = OPEN_XR.instance;
            GuiComponent.drawString(poseStack, font, instance.runtimeName + " " + instance.runtimeVersionString, x + 1, y + 12, 16777215 | fade);
            y += 12;
        }

        if (OPEN_XR.session != null) {
            OpenXRSystem system = OPEN_XR.session.system;
            for (String line : wordWrap(system.systemName, wrapLength)) {
                GuiComponent.drawString(poseStack, font, line, x + 1, y + 12, 16777215 | fade);
                y += 12;
            }
            GuiComponent.drawString(poseStack, font, I18n.get("openxr.form_factor." + system.formFactor), x + 1, y + 12, 16777215 | fade);
        } else {
            GuiComponent.drawString(poseStack, font, I18n.get("mcxr.menu.session_not_created"), x + 1, y + 12, 16777215 | fade);
            y += 12;
            if (OPEN_XR.createException != null) {
                String message = OPEN_XR.createException.getMessage();
                if (OPEN_XR.createException.result == XR10.XR_ERROR_FORM_FACTOR_UNAVAILABLE) {
                    message = I18n.get("mcxr.error.form_factor_unavailable");
                }
                for (String line : wordWrap(message, wrapLength)) {
                    GuiComponent.drawString(poseStack, Minecraft.getInstance().font, line, x + 1, y + 12, 16733525 | fade);
                    y += 12;
                }
                if (mouseX > x && mouseY < y + 10 && mouseY > screen.height / 4 + 48 + 12 + 10) {
                    screen.renderComponentTooltip(poseStack, wordWrapText(message, 40), mouseX + 14, mouseY);
                }
            }
        }
    }

    private static List<String> wordWrap(String string, int wrapLength) {
        return WordUtils.wrap(string, wrapLength, null, true).lines().toList();
    }

    private static List<Component> wordWrapText(String string, int wrapLength) {
        return WordUtils.wrap(string, wrapLength, null, true).lines().map(s -> (Component) (new TextComponent(s))).toList();
    }
}
