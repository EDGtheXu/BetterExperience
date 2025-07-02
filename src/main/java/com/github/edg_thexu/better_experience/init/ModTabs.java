package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public final class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Better_experience.MODID);

    public static final RegistryObject<CreativeModeTab> BETTER_EXPERIENCE = TABS.register("better_experience",
            () -> CreativeModeTab.builder().icon(() -> ModItems.MagicBoomStaff.get().asItem().getDefaultInstance())
                    .title(Component.translatable("creativetab.better_experience.item"))
                    .displayItems((parameters, output) -> {
                        ModItems.TOOLS.getEntries().forEach(block -> output.accept(block.get()));
                        ModItems.ITEMS.getEntries().forEach(block -> output.accept(block.get()));

                    }).build()
    );

}
