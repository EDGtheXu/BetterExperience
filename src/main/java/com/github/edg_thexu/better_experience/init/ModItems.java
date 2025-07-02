package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.item.MagicBoomStaff;
import com.github.edg_thexu.better_experience.item.PotionBag;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Better_experience.MODID);
    public static final DeferredRegister<Item> TOOLS = DeferredRegister.create(ForgeRegistries.ITEMS, Better_experience.MODID);

    public static RegistryObject<Item> MagicBoomStaff = TOOLS.register("magic_boom_staff", ()->new MagicBoomStaff(new Item.Properties().stacksTo(1), 3, 5));
    public static RegistryObject<Item> StarBoomStaff = TOOLS.register("star_boom_staff", ()->new MagicBoomStaff(new Item.Properties().stacksTo(1), 3, 10));
    public static RegistryObject<Item> PotionBag = TOOLS.register("potion_bag", ()->new PotionBag(new Item.Properties().stacksTo(1)));



    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        TOOLS.register(bus);
    }

}
