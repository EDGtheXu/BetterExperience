package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.client.event.ClientModEvent;
import com.github.edg_thexu.better_experience.menu.AutoFishMenu;
import com.github.edg_thexu.better_experience.menu.PotionBagMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;


/**
 * 注册屏幕: {@link ClientModEvent#onClientSetup})}
 */
public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Better_experience.MODID);

    public static final Supplier<MenuType<AutoFishMenu>> AUTO_FISH_MENU = TYPES.register("auto_fish", () -> new MenuType<>(AutoFishMenu::new, FeatureFlags.VANILLA_SET));
    public static final Supplier<MenuType<PotionBagMenu>> POTION_BAG_MENU = TYPES.register("potion_bag", () -> new MenuType<>(PotionBagMenu::new, FeatureFlags.VANILLA_SET));


}