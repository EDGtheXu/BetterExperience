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
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.common.item.food.BaseFoodItem;
import org.confluence.mod.common.item.potion.EffectPotionItem;

import java.lang.reflect.Method;
import java.util.function.Predicate;

public class PlayerInventoryManager {

    public int detectInternal;
    private static final int _detectInternal = 200;
    /**
     * 食物类 effectInstance 过滤器
     */
    private static final Predicate<MobEffectInstance> canApplyEffect = (effect) -> {
        Holder<MobEffect> effect1 = effect.getEffect();
        int time = effect.getDuration();
        if(effect1 == MobEffects.SATURATION || effect1 == MobEffects.ABSORPTION || effect1 == MobEffects.REGENERATION){
            // 饱和效果不应该应用，过于超模
            return false;
        }
        return true;
    };
    /**
     * 物品过滤器
     */
    public static Predicate<ItemStack> canApply = (stack) -> {
        Item item = stack.getItem();
        if(stack.getCount() < ServerConfig.AUTO_POTION_STACK_SIZE.get()) {
            // 配置文件
            return false;
        }
        if(item instanceof EffectPotionItem potion) {
            // 效果类药水
            return true;
        }
        if(item instanceof Item food)
        {
            //食物类
            var foodProperties = food.getFoodProperties(stack, null);
            if (foodProperties != null) {
                for (FoodProperties.PossibleEffect foodproperties$possibleeffect : foodProperties.effects()) {
                    var mobEffect = foodproperties$possibleeffect.effect();
                    if (canApplyEffect.test(mobEffect)) {
                        // 只要有一个可以应用的效果就返回true
                        return !foodProperties.effects().isEmpty();
                    }
                }
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
     * 客户端 检测可以作用的容器
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
     * 服务端 应用效果
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
            else if(item instanceof Item food){
                FoodProperties foodproperties = food.getFoodProperties(stack, player);
                if(foodproperties != null){
                    for (FoodProperties.PossibleEffect foodproperties$possibleeffect : foodproperties.effects()) {
                        var mobEffect = foodproperties$possibleeffect.effect();
                        if (canApplyEffect.test(mobEffect)) {
                            player.addEffect(mobEffect);
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
    @OnlyIn(Dist.CLIENT)
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
