package com.github.edg_thexu.better_experience.block;

import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.menu.AutoFishMenu;
import com.github.edg_thexu.better_experience.mixed.IFishingHook;
import com.github.edg_thexu.better_experience.mixed.IPlayer;
import com.github.edg_thexu.better_experience.module.autofish.AutoFishManager;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.mod.common.item.fishing.AbstractFishingPole;
import org.confluence.mod.common.item.fishing.BaitItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import static net.minecraft.world.level.block.BarrelBlock.FACING;

public class AutoFishBlock extends BaseEntityBlock {

    public AutoFishBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        FluidState fluidstate = placeContext.getLevel().getFluidState(placeContext.getClickedPos());
        return defaultBlockState()
                .setValue(FACING, placeContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AutoFishMachineEntity(blockPos, blockState);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(state.hasBlockEntity()){
            BlockEntity entity = level.getBlockEntity(pos);
            ((IPlayer)player).betterExperience$setInteractBlockEntity(entity);
            if(!level.isClientSide) {
                player.openMenu(state.getMenuProvider(level, pos));
            }
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, ModBlocks.AUTO_FISH_BLOCK_ENTITY.get(),  (level, pos, state, entity)-> {

            // 未开始
            if(entity.dataAccess.get(0) == 0)
                return;

            // 启动游戏重新加载
            Player player = entity.owner;
            if(player == null && entity.ownerUUID != null && level instanceof ServerLevel serverLevel){
                entity.owner = (Player) serverLevel.getEntity(entity.ownerUUID);
                entity.tryStart(entity.owner);
            }
            // 尝试启动
            if(entity.dataAccess.get(0) == 1){
                entity.tryStart(player);
            }


            if(level instanceof ServerLevel serverLevel && player!= null && entity.target != null && entity.lastTick + entity.fishingTime < ++entity.ticks){

                ItemStack poleStack = entity.getItem(27);
                ItemStack bait = entity.getItem(28);
                ItemStack curios = entity.getItem(29);

                if(poleStack.getItem() instanceof FishingRodItem pole) {
                    FishingHook hook;

                    try {
                        float power = AutoFishManager.computeFishingPower(null, poleStack,
                                bait.getItem() instanceof BaitItem ? (BaitItem) bait.getItem() : null,
                                curios);

                        if(pole instanceof  AbstractFishingPole pole1) {
                            Method funcField = AbstractFishingPole.class.getDeclaredMethod("getHook", ItemStack.class, Player.class, Level.class, int.class, int.class);
                            funcField.setAccessible(true);
                            hook = (FishingHook) funcField.invoke(pole, poleStack, player, level, (int) power, 5);
                        }
                        else{
                            hook = new FishingHook(player, level, (int) power, 5);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }

                    ((IFishingHook) hook).betterExperience$setPos(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                    ((IFishingHook) hook).betterExperience$setSimulation(true);

                    hook.setPos(entity.target);

                    try {
                        Field nibbleField = FishingHook.class.getDeclaredField("nibble");
                        nibbleField.setAccessible(true);
                        nibbleField.setInt(hook, 10);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    // 模拟收杆
                    hook.retrieve(poleStack);
                    List<ItemStack> items = ((IFishingHook)hook).betterExperience$getItems();

                    for(ItemStack item : items){
                        // 尝试堆叠
                        for(int i=0;i<27;i++){
                            ItemStack current = entity.getItem(i);
                            if(current.isEmpty()){
                                entity.setItem(i, item.copy());
                                item.setCount(0);
                                break;
                            }
                            if (current.is(item.getItem()) && current.getCount() < current.getMaxStackSize()) {
                                // 计算可以堆叠的数量
                                int transfer = Math.min(item.getCount(), current.getMaxStackSize() - current.getCount());
                                current.grow(transfer); // 增加当前物品的数量
                                item.shrink(transfer);  // 减少新物品的数量
                                if (item.isEmpty()) {
                                    break; // 如果新物品被完全堆叠，返回true
                                }
                            }
                        }
                        // 溢出
                        if(!item.isEmpty()){
                            var f = state.getValue(FACING);
                            float dir = f.getRotation().angle();
                            float r = 0.1f;
                            ItemEntity itemEntity = new ItemEntity(level,
                                    pos.getX() + 0.5 + r * Math.sin(dir),
                                    pos.getY() + 1,
                                    pos.getZ() + 0.5 + r * Math.cos(dir), item);
                            level.addFreshEntity(itemEntity);
                        }
                    }

                    entity.lastTick = entity.ticks;

                    // TODO 消耗鱼饵, 计算下一次上钩时间  ( 20 tick )

                    entity.fishingTime = (int) (20 * 10 * entity.cdReduce);

                }else{
                    // 没有钓竿，终止
                    entity.dataAccess.set(0, 0);
                }
            }


        });
    }

    public static class AutoFishMachineEntity extends ChestBlockEntity {


        int ticks = 0;
        // 上一次上钩时间
        int lastTick = 0;
        // 下次上钩时间
        int fishingTime = 0;
        // 冷却缩减
        float cdReduce = 1;

        Player owner;
        UUID ownerUUID;
        // 水中鱼钩位置
        Vec3 target;
        /**
         * 简单状态：
         * <p>0：未开始
         * <p>1：尝试启动
         * <p>2：运行中
         */
        public int isStarted = 0;
        private final ContainerData dataAccess;

        public AutoFishMachineEntity(BlockPos pos, BlockState blockState) {
            super(ModBlocks.AUTO_FISH_BLOCK_ENTITY.get(), pos, blockState);

            this.dataAccess = new ContainerData() {
                public int get(int id) {
                    return switch (id) {
                        case 0 -> isStarted;
                        case 1->  (lastTick + fishingTime - ticks) / 20;
                        default -> 0;
                    };
                }
                public void set(int id, int value) {
                    switch (id) {
                        case 0 -> isStarted = value;
                        case 1 -> cdReduce = value * 0.01f;
                    }

                }
                public int getCount() {
                    return 2;
                }
            };

            if(this.getItems().size() < getContainerSize()){
                this.setItems(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
            }
        }

        public boolean tryStart(Player player){
            if(this.findTarget()){
                this.owner = player;
                player.sendSystemMessage(Component.literal("start"));
                this.isStarted = 2;
                return true;
            }else{
                player.sendSystemMessage(Component.literal("no water"));
                this.isStarted = 0;
                return false;
            }
        }

        // todo : 寻找水中位置
        private boolean findTarget(){
            BlockPos pos = this.getBlockPos();
            // 水下正下方
            for(int i = 1; i < 10; i++){
                if(this.getLevel().getBlockState(pos.below(i)).is(Blocks.WATER)){
                    target = new Vec3(pos.getX() + 0.5, pos.getY() + i, pos.getZ() + 0.5);
                    return true;
                }
            }

            return false;
        }

        @Override
        public int getContainerSize() {
            return 30;
        }

        @Override
        protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
            return new AutoFishMenu(id, inventory, this, this.dataAccess);
        }

        @Override
        public Packet<ClientGamePacketListener> getUpdatePacket() {
            return ClientboundBlockEntityDataPacket.create(this);
        }

        @Override
        public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
            super.onDataPacket(net, pkt, lookupProvider);
            CompoundTag tag = pkt.getTag();
            isStarted = tag.getInt("started");
            if(tag.contains("owner")){
                this.ownerUUID = tag.getUUID("owner");
            }
        }

        @Override
        protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            this.setItems(NonNullList.withSize(getContainerSize(), ItemStack.EMPTY));
            super.loadAdditional(tag, registries);
            isStarted = tag.getInt("started");
            if(tag.contains("owner")){
                this.ownerUUID = tag.getUUID("owner");
            }
        }

        @Override
        public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
            CompoundTag tag = super.getUpdateTag(registries);
            tag.putInt("started", isStarted);
            if(owner != null)
                tag.putUUID("owner", owner.getUUID());
            return tag;
        }

        @Override
        protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
            super.saveAdditional(tag, registries);
            tag.putInt("started", isStarted);
            if(owner != null)
                tag.putUUID("owner", owner.getUUID());
        }

    }
}
