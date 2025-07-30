package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.better_experience.item.DebugItem;
import com.github.edg_thexu.better_experience.item.MagicBoomStaff;
import com.github.edg_thexu.better_experience.item.PotionBag;
import com.github.edg_thexu.better_experience.item.UniversalController;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Better_experience.MODID);
    public static final DeferredRegister.Items TOOLS = DeferredRegister.createItems(Better_experience.MODID);

    public static DeferredItem<Item> MagicBoomStaff = TOOLS.register("magic_boom_staff", ()->new MagicBoomStaff(new Item.Properties().stacksTo(1), 3, 5));
    public static DeferredItem<Item> StarBoomStaff = TOOLS.register("star_boom_staff", ()->new MagicBoomStaff(new Item.Properties().stacksTo(1), 3, 10));
    public static DeferredItem<Item> PotionBag = TOOLS.register("potion_bag", ()->new PotionBag(new Item.Properties().stacksTo(1).component(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT, new ItemContainerComponent(18))));
    public static DeferredItem<Item> DebugItem = TOOLS.register("debug_item", ()->new DebugItem(new Item.Properties()));
    public static DeferredItem<Item> UniversalController = TOOLS.register("universal_controller", ()->new UniversalController(new Item.Properties()));



    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        TOOLS.register(bus);
    }

}
