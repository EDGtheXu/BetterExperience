package com.github.edg_thexu.better_experience.block;

import com.github.edg_thexu.better_experience.init.ModBlocks;
import com.github.edg_thexu.better_experience.menu.AutoFishMenu;
import com.github.edg_thexu.better_experience.mixed.IFishingHook;
import com.github.edg_thexu.better_experience.mixed.IPlayer;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.confluence.mod.common.item.fishing.AbstractFishingPole;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class AutoFishBlock extends BaseEntityBlock {

    public AutoFishBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
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

                if(poleStack.getItem() instanceof AbstractFishingPole pole) {
                    FishingHook hook;
                    try {
                        Method funcField = AbstractFishingPole.class.getDeclaredMethod("getHook", ItemStack.class, Player.class, Level.class, int.class, int.class);
                        funcField.setAccessible(true);
                        // todo : 计算渔力
                        hook = (FishingHook) funcField.invoke(pole, poleStack, player, level, 50, 5);
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

                    hook.retrieve(poleStack);
                    entity.lastTick = entity.ticks;

                    // TODO 消耗鱼饵, 计算下一次上钩时间  ( 20 tick )

                    entity.fishingTime = 20;

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
                        default -> 0;
                    };
                }
                public void set(int id, int value) {
                    switch (id) {
                        case 0 -> isStarted = value;
                    }

                }
                public int getCount() {
                    return 1;
                }
            };
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
