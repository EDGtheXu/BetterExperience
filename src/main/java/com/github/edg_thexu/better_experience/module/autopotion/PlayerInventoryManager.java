package com.github.edg_thexu.better_experience.module.autopotion;

import com.github.edg_thexu.better_experience.config.ServerConfig;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import com.github.edg_thexu.better_experience.network.C2S.PotionApplyPacketC2S;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.common.item.food.BaseFoodItem;
import org.confluence.mod.common.item.potion.EffectPotionItem;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public class PlayerInventoryManager {

    public int detectInternal;
    private static final int _detectInternal = 1000;
    public static Predicate<ItemStack> canApply = (stack) -> {
        Item item = stack.getItem();
        if(stack.getCount() < ServerConfig.AUTO_POTION_STACK_SIZE.get())
            return false;
        if(item instanceof EffectPotionItem potion)
            return true;
        if(item instanceof BaseFoodItem food)
        {
            var foodProperties = food.getFoodProperties(stack, null);
            if (foodProperties != null) {


                return !foodProperties.effects().isEmpty();
            }
        }
        return false;
    };

    private static PlayerInventoryManager instance;

    public static PlayerInventoryManager getInstance(){
        if(instance == null)
            instance = new PlayerInventoryManager();
        return instance;
    }

    /**
     * 检测可以作用的容器
     * @param player
     */
    public void detect(Player player){
        // 客户端检测，配置是否启用，检测间隔
        if(!player.level().isClientSide() || !ServerConfig.AUTO_POTION_OPEN.get() || --detectInternal > 0)
            return;

        detectInternal = _detectInternal;

        Inventory inventory = player.getInventory();
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            try {
                checkItem(inventory.getItem(i), player);
            }catch (Exception ignored){

            }
        }

        var items = player.getData(ModAttachments.ENDER_CHEST).getItems();

        for (Item item : items) {
            try {
                PacketDistributor.sendToServer(new PotionApplyPacketC2S(item.getDefaultInstance()));
            } catch (Exception ignored) {

            }
        }
    }

    private void checkItem(ItemStack stack, Player player){
        if(canApply.test(stack)){
            PacketDistributor.sendToServer(new PotionApplyPacketC2S(stack));
        }
    }

    /**
     * 应用效果
     * @param stack
     * @param player
     */
    public static void apply(ItemStack stack, Player player){
        try {
            Level level = player.level();
            Item item = stack.getItem();
            if(item instanceof EffectPotionItem potion){
                Method method = potion.getClass().getDeclaredMethod("apply", ItemStack.class, Level.class, LivingEntity.class);
                method.setAccessible(true);
                method.invoke(potion, stack, player.level(), player);
            }
            else if(item instanceof BaseFoodItem food){
                FoodProperties foodproperties = food.getFoodProperties(stack, player);
                if(foodproperties != null){
                    for (FoodProperties.PossibleEffect foodproperties$possibleeffect : foodproperties.effects()) {
                        if (player.getRandom().nextFloat() < foodproperties$possibleeffect.probability()) {
                            player.addEffect(foodproperties$possibleeffect.effect());
                        }
                    }
                }


            }
        }catch (Exception ignored){

        }
    }

    /**
     * 渲染物品应用的背景
     * @param screen
     * @param stack
     * @param guiGraphics
     * @param x
     * @param y
     * @param partialTick
     */
    public static void renderApply(AbstractContainerScreen screen,ItemStack stack,  GuiGraphics guiGraphics, int x, int y, float partialTick){

        if((
                screen instanceof InventoryScreen ||
                screen instanceof CreativeModeInventoryScreen ||
                screen instanceof ContainerScreen && screen.getTitle().toString().contains("enderchest")
        )
                && canApply.test(stack)){

            guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + 16, y + 16, 0x00200800, 0xf020FFF0, 0);

        }
    }

}
