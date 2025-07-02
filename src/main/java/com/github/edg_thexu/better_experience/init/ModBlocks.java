package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.block.AutoFishBlock;
import com.github.edg_thexu.better_experience.block.AutoSellBlock;
import com.github.edg_thexu.better_experience.block.ReforgeBlock;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import java.util.function.BiFunction;
import java.util.function.Supplier;


public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Better_experience.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Better_experience.MODID);;

    public static final RegistryObject<AutoFishBlock> AUTO_FISH_BLOCK = registerSingleBlock("auto_fish_block", ()-> new AutoFishBlock(Block.Properties.copy(Blocks.STONE).lightLevel(state->state.getValue(AutoFishBlock.TURN)? 10 : 0)));
    public static final Supplier<BlockEntityType<AutoFishBlock.AutoFishMachineEntity>> AUTO_FISH_BLOCK_ENTITY = registerBlockEntity("auto_fish_block_entity", () -> BlockEntityType.Builder.of(AutoFishBlock.AutoFishMachineEntity::new, AUTO_FISH_BLOCK.get()).build(null));
    public static final RegistryObject<BlockItem> AUTO_FISH_BLOCK_ITEM = ModItems.ITEMS.register("auto_fish_block", () -> new AutoFishBlock.Item(AUTO_FISH_BLOCK.get()));

    // 以下是加载confluence以后才会注册
    public static final RegistryObject<AutoSellBlock> AUTO_SELL_BLOCK = registerDecoration("auto_sell_block", ()-> new AutoSellBlock(Block.Properties.copy(Blocks.STONE).noOcclusion()), ModBlocks::registerWithItem, ConfluenceHelper::isLoaded);
    public static final Supplier<BlockEntityType<AutoSellBlock.AutoSellBlockEntity>> AUTO_SELL_BLOCK_ENTITY = registerBlockEntityDecoration("auto_sell_block_entity", () -> BlockEntityType.Builder.of(AutoSellBlock.AutoSellBlockEntity::new, AUTO_SELL_BLOCK.get()).build(null), ModBlocks::registerBlockEntity, ConfluenceHelper::isLoaded);

    public static final RegistryObject<ReforgeBlock> REFORGE_BLOCK = registerDecoration("reforge_block", ()-> new ReforgeBlock(Block.Properties.copy(Blocks.STONE)), ModBlocks::registerWithItem, ConfluenceHelper::isLoaded);
    public static final Supplier<BlockEntityType<ReforgeBlock.ReforgeBlockEntity>> REFORGE_BLOCK_ENTITY = registerBlockEntityDecoration("reforge_block_entity", () -> BlockEntityType.Builder.of(ReforgeBlock.ReforgeBlockEntity::new, REFORGE_BLOCK.get()).build(null), ModBlocks::registerBlockEntity, ConfluenceHelper::isLoaded);


    private static <B extends Block> RegistryObject<B> registerWithItem(String id, Supplier<B> block) {
        RegistryObject<B> object = BLOCKS.register(id, block);
        ModItems.ITEMS.register(id, ()->new BlockItem(object.get(), new Item.Properties()));
        return object;
    }


    private static <B extends Block> RegistryObject<B> registerSingleBlock(String id, Supplier<B> block) {
        RegistryObject<B> object = BLOCKS.register(id, block);
        return object;
    }

    public static <B extends BlockEntityType<?>> RegistryObject<B> registerBlockEntity(String id, Supplier<? extends B> blockEntity){
        return BLOCK_ENTITIES.register(id, blockEntity);
    }

    private static <B extends Block> RegistryObject<B> registerDecoration(String id, Supplier<B> block, BiFunction<String, Supplier<B>, RegistryObject<B>> register, Supplier<Boolean> condition) {
        if(condition.get()) {
            return register.apply(id, block);
        }
        return null;
    }

    public static <B extends BlockEntityType<?>> RegistryObject< B> registerBlockEntityDecoration(
            String id,
            Supplier<? extends B> blockEntity,
            BiFunction<String, Supplier<? extends B>, RegistryObject<B>> register,
            Supplier<Boolean> condition){
        if(condition.get()) {
            return register.apply(id, blockEntity);
        }
        return null;
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
    }

}
