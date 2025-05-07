
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.interplay.simple_incubator.init;

import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.interplay.simple_incubator.client.gui.StorageRightGUIScreen;
import net.interplay.simple_incubator.client.gui.StorageMiddleGUIScreen;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SimpleIncubatorModScreens {
	@SubscribeEvent
	public static void clientLoad(RegisterMenuScreensEvent event) {
		event.register(SimpleIncubatorModMenus.STORAGE_MIDDLE_GUI.get(), StorageMiddleGUIScreen::new);
		event.register(SimpleIncubatorModMenus.STORAGE_RIGHT_GUI.get(), StorageRightGUIScreen::new);
	}
}
