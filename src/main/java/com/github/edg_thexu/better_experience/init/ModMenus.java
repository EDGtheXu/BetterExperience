package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.client.event.ClientModEvent;
import com.github.edg_thexu.better_experience.menu.AutoFishMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;


/**
 * 注册屏幕: {@link ClientModEvent#registerMenuScreens})}
 */
public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Better_experience.MODID);

    public static final Supplier<MenuType<AutoFishMenu>> AUTO_FISH_MENU = TYPES.register("auto_fish", () -> new MenuType<>(AutoFishMenu::new, FeatureFlags.VANILLA_SET));


}