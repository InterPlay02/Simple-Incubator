package net.interplay.simple_incubator.client.gui;

import net.neoforged.neoforge.network.PacketDistributor;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.util.Mth;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.GuiGraphics;

import net.interplay.simple_incubator.world.inventory.IncubationTransmitterGUIMenu;
import net.interplay.simple_incubator.procedures.SpeedLabelProcedure;
import net.interplay.simple_incubator.procedures.ShowSpeedButtonProcedure;
import net.interplay.simple_incubator.procedures.GreenLightProcedure;
import net.interplay.simple_incubator.procedures.AntenaLoadingBarProcedure;
import net.interplay.simple_incubator.network.IncubationTransmitterGUIButtonMessage;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class IncubationTransmitterGUIScreen extends AbstractContainerScreen<IncubationTransmitterGUIMenu> {
	private final static HashMap<String, Object> guistate = IncubationTransmitterGUIMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	ImageButton imagebutton_speedbutton;

	public IncubationTransmitterGUIScreen(IncubationTransmitterGUIMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = ResourceLocation.parse("simple_incubator:textures/screens/incubation_transmitter_gui.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
		if (ShowSpeedButtonProcedure.execute(entity))
			if (mouseX > leftPos + 72 && mouseX < leftPos + 96 && mouseY > topPos + 60 && mouseY < topPos + 84)
				guiGraphics.renderTooltip(font, Component.translatable("gui.simple_incubator.incubation_transmitter_gui.tooltip_adjust_the_spawn_frequency_max"), mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

		guiGraphics.blit(ResourceLocation.parse("simple_incubator:textures/screens/antenaloadingbar.png"), this.leftPos + 9, this.topPos + 26, 0, Mth.clamp((int) AntenaLoadingBarProcedure.execute(world, x, y, z) * 32, 0, 288), 64, 32, 64, 320);

		guiGraphics.blit(ResourceLocation.parse("simple_incubator:textures/screens/red_and_green_lights.png"), this.leftPos + 5, this.topPos + 66, 0, Mth.clamp((int) GreenLightProcedure.execute(world, x, y, z) * 12, 0, 12), 12, 12, 12, 24);

		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
		guiGraphics.drawString(this.font, Component.translatable("gui.simple_incubator.incubation_transmitter_gui.label_incubation_transmitter"), 5, 5, -12829636, false);
		guiGraphics.drawString(this.font, Component.translatable("gui.simple_incubator.incubation_transmitter_gui.label_sending"), 17, 69, -12829636, false);
		if (ShowSpeedButtonProcedure.execute(entity))
			guiGraphics.drawString(this.font, Component.translatable("gui.simple_incubator.incubation_transmitter_gui.label_spawn_speed"), 116, 64, -12829636, false);
		if (ShowSpeedButtonProcedure.execute(entity))
			guiGraphics.drawString(this.font,

					SpeedLabelProcedure.execute(world, x, y, z), 97, 64, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		imagebutton_speedbutton = new ImageButton(this.leftPos + 80, this.topPos + 60, 16, 16,
				new WidgetSprites(ResourceLocation.parse("simple_incubator:textures/screens/speedbutton.png"), ResourceLocation.parse("simple_incubator:textures/screens/speedbuttonhover.png")), e -> {
					if (ShowSpeedButtonProcedure.execute(entity)) {
						PacketDistributor.sendToServer(new IncubationTransmitterGUIButtonMessage(0, x, y, z));
						IncubationTransmitterGUIButtonMessage.handleButtonAction(entity, 0, x, y, z);
					}
				}) {
			@Override
			public void renderWidget(GuiGraphics guiGraphics, int x, int y, float partialTicks) {
				if (ShowSpeedButtonProcedure.execute(entity))
					guiGraphics.blit(sprites.get(isActive(), isHoveredOrFocused()), getX(), getY(), 0, 0, width, height, width, height);
			}
		};
		guistate.put("button:imagebutton_speedbutton", imagebutton_speedbutton);
		this.addRenderableWidget(imagebutton_speedbutton);
	}
}
