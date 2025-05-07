
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.interplay.simple_incubator.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.registries.Registries;

import net.interplay.simple_incubator.world.inventory.StorageRightGUIMenu;
import net.interplay.simple_incubator.world.inventory.StorageMiddleGUIMenu;
import net.interplay.simple_incubator.SimpleIncubatorMod;

public class SimpleIncubatorModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, SimpleIncubatorMod.MODID);
	public static final DeferredHolder<MenuType<?>, MenuType<StorageMiddleGUIMenu>> STORAGE_MIDDLE_GUI = REGISTRY.register("storage_middle_gui", () -> IMenuTypeExtension.create(StorageMiddleGUIMenu::new));
	public static final DeferredHolder<MenuType<?>, MenuType<StorageRightGUIMenu>> STORAGE_RIGHT_GUI = REGISTRY.register("storage_right_gui", () -> IMenuTypeExtension.create(StorageRightGUIMenu::new));
}
