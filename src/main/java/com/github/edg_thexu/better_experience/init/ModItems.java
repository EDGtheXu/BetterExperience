package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.item.MagicBoomStaff;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Better_experience.MODID);
    public static final DeferredRegister.Items TOOLS = DeferredRegister.createItems(Better_experience.MODID);

    public static DeferredItem<Item> MagicBoomStaff = TOOLS.register("magic_boom_staff", ()->new MagicBoomStaff(new Item.Properties(), 3, 5));
    public static DeferredItem<Item> StarBoomStaff = TOOLS.register("star_boom_staff", ()->new MagicBoomStaff(new Item.Properties(), 3, 10));



    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        TOOLS.register(bus);
    }

}
