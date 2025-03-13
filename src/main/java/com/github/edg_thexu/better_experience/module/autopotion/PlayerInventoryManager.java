package com.github.edg_thexu.better_experience.module.autopotion;

import com.github.edg_thexu.better_experience.attachment.AutoPotionAttachment;
import com.github.edg_thexu.better_experience.config.ServerConfig;
import com.github.edg_thexu.better_experience.init.ModAttachments;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.mod.client.gui.container.ExtraInventoryScreen;
import org.confluence.mod.common.item.potion.EffectPotionItem;
import oshi.util.tuples.Pair;
import top.theillusivec4.curios.client.gui.CuriosScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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
    public static Function<ItemStack, List<Pair<Holder<MobEffect>, Integer>>> getApplyEffect = (stack) -> {
        Item item = stack.getItem();
        List<Pair<Holder<MobEffect>, Integer>> effects = new ArrayList<>();
        if(stack.getCount() < ServerConfig.AUTO_POTION_STACK_SIZE.get()) {
            // 配置文件
            return effects;
        }
        if(item instanceof EffectPotionItem potion) {
            // 效果类药水
            effects.add(new Pair<>(potion.mobEffect, potion.amplifier));
            return effects;
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
                        return foodProperties.effects().stream().map(eff->{
                            var ins = eff.effect();
                            return new Pair<>(ins.getEffect(), ins.getAmplifier());
                        }).toList();
                    }
                }
            }
        }
        return effects;
    };

    public static Predicate<ItemStack> canApply = (stack) -> !getApplyEffect.apply(stack).isEmpty();

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

        detectInternal = (int) (_detectInternal * 0.1f);

        // 背包的药水
        List<Pair<Holder<MobEffect>, Integer>> effects = new ArrayList<>();
        Inventory inventory = player.getInventory();
        for(int i = 0; i < inventory.getContainerSize(); i++) {
            try {
                ItemStack stack = inventory.getItem(i);
                effects.addAll(getApplyEffect.apply(stack));
            }catch (Exception ignored){

            }
        }

        // 末影箱的药水
        var items = player.getData(ModAttachments.ENDER_CHEST).getItems();
        for (Item item : items) {
            try {
                ItemStack stack = new ItemStack(item, ServerConfig.AUTO_POTION_STACK_SIZE.get());
                effects.addAll(getApplyEffect.apply(stack));
            }catch (Exception ignored){

            }
        }

        // 重新生成缓存
        var data = player.getData(ModAttachments.AUTO_POTION);
        data.getPotions().clear();
        effects.forEach(effect_amp -> {
            data.addPotion(effect_amp.getA(), effect_amp.getB());
        });

        // 同步数据
        data.sync();
    }

    /**
     * 服务端 应用效果
     * @param player
     */
    public static void apply(AutoPotionAttachment attachment, Player player){
        try {
            var data = player.getData(ModAttachments.AUTO_POTION);
            data.getPotions().forEach((effect1, amp1) ->{
                if(!attachment.getPotions().containsKey(effect1))
                    player.removeEffect(effect1);
            } );
            attachment.getPotions().forEach((effect,amp)->{
                player.addEffect(new MobEffectInstance(effect, -1, amp, false, false));
            });
            player.setData(ModAttachments.AUTO_POTION, attachment);
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
                screen instanceof ContainerScreen && screen.getTitle().toString().contains("enderchest") ||
                        screen instanceof ExtraInventoryScreen||
                        screen instanceof CuriosScreen
        )
                && canApply.test(stack)){

            guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + 16, y + 16, 0x00200800, 0xf020FFF0, 0);

        }
    }

}
