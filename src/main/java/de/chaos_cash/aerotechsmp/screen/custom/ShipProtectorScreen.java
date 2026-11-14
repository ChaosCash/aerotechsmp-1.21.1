package de.chaos_cash.aerotechsmp.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import de.chaos_cash.aerotechsmp.AerotechSMP;
import de.chaos_cash.aerotechsmp.util.ChatHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ShipProtectorScreen extends AbstractContainerScreen<ShipProtectorMenu> {
    private int guiX;
    private int guiY;

    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "textures/gui/ship_protector/ship_protector_gui.png");
    private static final ResourceLocation ENERGY_BAR =
            ResourceLocation.fromNamespaceAndPath(AerotechSMP.MOD_ID, "textures/gui/ship_protector/energy_bar.png");

    public ShipProtectorScreen(ShipProtectorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        imageWidth = 176;
        imageHeight = 186;
        inventoryLabelY = 92;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        guiX = (width - imageWidth) / 2;
        guiY = (height - imageHeight) / 2;

        guiGraphics.blit(GUI_TEXTURE, guiX, guiY, 0, 0, imageWidth, imageHeight);
        drawEnergyBar(guiGraphics);
    }

    private void drawEnergyBar(GuiGraphics guiGraphics) {
        int x = guiX + 8;
        int y = guiY + 18;
        int width = 48;
        int height = 70;

        guiGraphics.blit(ENERGY_BAR, x, y,  width / 2, 0, width / 2, height, width, height);
        guiGraphics.blit(ENERGY_BAR, x, y,  0, 0, width / 2, height, width, height);
        ChatHandler.broadcast("" + menu.blockEntity.getEnergyStorage().getEnergyStored());
        ChatHandler.broadcast("" + menu.blockEntity.getEnergyStorage().getMaxEnergyStored());
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
