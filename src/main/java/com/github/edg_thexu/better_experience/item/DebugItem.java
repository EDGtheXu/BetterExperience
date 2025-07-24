package com.github.edg_thexu.better_experience.item;

import com.github.edg_thexu.better_experience.client.gui.editor.ObjectEditorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DebugItem extends Item {

    public DebugItem(Properties properties) {
        super(properties);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {

        if(level.isClientSide()){
            Minecraft.getInstance().setScreen(new ObjectEditorScreen(Component.literal("Editor") ));
            return InteractionResultHolder.success(player.getItemInHand(usedHand));

        }
        return super.use(level, player, usedHand);
    }
}