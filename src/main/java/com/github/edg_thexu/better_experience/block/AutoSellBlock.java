package com.github.edg_thexu.better_experience.block;

import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.confluence.mod.common.component.ValueComponent;
import org.confluence.mod.common.init.ModTags;
import org.confluence.mod.util.PlayerUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

import static net.minecraft.world.level.block.ChestBlock.FACING;

public class AutoSellBlock extends BaseEntityBlock {
    public AutoSellBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    public static final MapCodec<AutoSellBlock> CODEC = simpleCodec(AutoSellBlock::new);
    public static final EnumProperty<ChestType> TYPE = BlockStateProperties.CHEST_TYPE;

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AutoSellBlockEntity(blockPos, blockState);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(state.hasBlockEntity()){
            BlockEntity entity = level.getBlockEntity(pos);
            if(!level.isClientSide && entity instanceof AutoSellBlockEntity entity1) {
                player.openMenu(new SimpleMenuProvider(entity1, Component.translatable(ModBlocks.AUTO_SELL_BLOCK.get().getDescriptionId())));
            }
        }
        return ItemInteractionResult.SUCCESS;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, TYPE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        return defaultBlockState()
                .setValue(TYPE, ChestType.SINGLE)
                .setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? createTickerHelper(pBlockEntityType, ModBlocks.AUTO_SELL_BLOCK_ENTITY.get(),  (level, pos, state, entity1)-> {

        }) : createTickerHelper(pBlockEntityType, ModBlocks.AUTO_SELL_BLOCK_ENTITY.get(),  (level, pos, state, entity1)-> {

            if(entity1 instanceof AutoSellBlockEntity entity) {
                if(level.dayTime() % 64 == 32){
                    ModUtils.unionItemStacks(entity.getItems());
                }
                if (level.dayTime() % 64 == 0) {
//                if(ChestBlockEntity.getOpenCount(level, pos) <= 0){

//                }

                    for (int i = 0; i < 27; i++) {
                        ItemStack stack = entity.getItem(i);

                        if (!stack.isEmpty() && !stack.is(ModTags.Items.COINS)) {
                            int money = ValueComponent.getValue(stack,0) / stack.getCount();
                            if (money > 0) {
                                entity.getItem(i).shrink(1);
                                int[] coins = PlayerUtils.decodeCoin(money);
                                for (int j = 0; j < 4; j++) {
                                    if (coins[j] <= 0) {
                                        continue;
                                    }
                                    Item item = PlayerUtils.INDEX_2_COIN.apply(j);
                                    int k;
                                    for (k = 0; k < 27; k++) {
                                        ItemStack candidate = entity.getItem(k);
                                        if (candidate.is(ModTags.Items.COINS)) {
                                            int index = PlayerUtils.COIN_2_INDEX.applyAsInt(candidate.getItem());
                                            if (candidate.getCount() == 100 && index > 0) {
                                                entity.setItem(k, new ItemStack(PlayerUtils.INDEX_2_COIN.apply(3 - (--index))));
                                            }
                                        }

                                        if (candidate.isEmpty()) {
                                            entity.setItem(k, new ItemStack(item, coins[j]));
                                            break;
                                        }
                                        if (candidate.getItem() == item) {
                                            int shrink = Math.min(coins[j], candidate.getMaxStackSize() - candidate.getCount());
                                            candidate.grow(shrink);
                                            coins[j] -= shrink;
                                            if (coins[j] == 0) {
                                                break;
                                            }
                                        }
                                    }
                                    if (k == 27) {
                                        level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() - 1, pos.getZ(), new ItemStack(item, coins[j])));
                                    }
                                }
                                break;
                            }
                        }
                    }

                }
            }

        });
    }

    public static class AutoSellBlockEntity extends ChestBlockEntity implements WorldlyContainer {

        int[] face;
        public AutoSellBlockEntity(BlockPos pos, BlockState blockState) {
            super(ModBlocks.AUTO_SELL_BLOCK_ENTITY.get(), pos, blockState);
            face = IntStream.range(0, 27).toArray();
            this.openersCounter = new ContainerOpenersCounter() {
                protected void onOpen(Level level, BlockPos blockPos, BlockState state) {

                }

                protected void onClose(Level level, BlockPos blockPos, BlockState state) {

                }

                protected void openerCountChanged(Level level, BlockPos blockPos, BlockState state, int id, int param) {
                    signalOpenCount(level, blockPos, state, id, param);
                }

                protected boolean isOwnContainer(Player player) {
                    if (!(player.containerMenu instanceof ChestMenu)) {
                        return false;
                    } else {
                        Container container = ((ChestMenu)player.containerMenu).getContainer();
                        return container == AutoSellBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(AutoSellBlockEntity.this);
                    }
                }
            };
        }

        @Override
        public int[] getSlotsForFace(Direction direction) {
            return face;
        }

        @Override
        public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
            return direction != Direction.DOWN && !itemStack.is(ModTags.Items.COINS);
        }

        @Override
        public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
            return direction == Direction.DOWN && itemStack.is(ModTags.Items.COINS);
        }

        public NonNullList<ItemStack> getItems() {
            return super.getItems();
        }

    }

}