package com.github.edg_thexu.better_experience.item;

import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.better_experience.init.ModDataComponentTypes;
import com.github.edg_thexu.better_experience.menu.PotionBagMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class PotionBag extends Item {
    public PotionBag(Properties properties) {
        super(properties);
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
//        ItemStack stack = player.getItemInHand(usedHand);
//        if(!level.isClientSide() && usedHand == InteractionHand.MAIN_HAND) {
//            var data = stack.get(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT);
//            if (data == null) {
//                data = new ItemContainerComponent(18);
//                stack.set(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT,data);
//            }
//            ItemContainerComponent finalData = data;
//            player.openMenu(new SimpleMenuProvider((id, inventory, p) -> new PotionBagMenu(id, inventory, finalData), Component.translatable(stack.getDescriptionId()).withColor(0x333333)));
//
//        }
//
//        return InteractionResultHolder.success(player.getItemInHand(usedHand));
//    }
//
//    @Override
//    public void appendHoverText(ItemStack stack, Level context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
//        tooltipComponents.add(Component.translatable("better_experience.tooltip.potion_bag.info"));
//        var data = stack.get(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT);
//        if (data != null) {
//            int size = data.getContainerSize();
//            int filled = 0;
//            for( var item : data.getItems()){
//                if(!item.isEmpty()){
//                    filled++;
//                }
//            }
//            MutableComponent component = Component.literal("(" + filled + "/" +size  + ")");
//            if(filled >= size){
//                component.withStyle(style -> style.withColor(0xFF0000));
//            }
//            tooltipComponents.add(component);
//        }
//
//    }

}
