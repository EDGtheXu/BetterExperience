package com.github.edg_thexu.better_experience.module.autopotion;

import com.github.edg_thexu.better_experience.attachment.AutoPotionAttachment;
import com.github.edg_thexu.better_experience.client.gui.container.PotionBagScreen;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.data.component.ItemContainerComponent;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import com.github.edg_thexu.better_experience.init.ModDataComponentTypes;
import com.github.edg_thexu.better_experience.init.ModItems;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.intergration.curios.CuriosHelper;
import com.github.edg_thexu.better_experience.menu.PotionBagMenu;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.lib.common.PlayerContainer;
import org.confluence.mod.client.gui.container.ExtraInventoryScreen;
import org.confluence.mod.common.init.ModAttachmentTypes;
import org.confluence.mod.common.init.ModTags;
import org.confluence.mod.common.init.block.FunctionalBlocks;
import org.confluence.mod.common.item.potion.EffectPotionItem;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;
import top.theillusivec4.curios.client.gui.CuriosScreen;

import java.util.*;

/**
 * 客户端遍历玩家背包，检测药水续杯等效果
 */
public class PlayerInventoryManager {

    public int detectInternal;
    private static final int _detectInternal = 200;
    public boolean serverOpenAutoPotion = true;
    /**
     * 食物类 effectInstance 过滤器
     */
    private static boolean canApplyEffect(MobEffectInstance effect)  {
        Holder<MobEffect> effect1 = effect.getEffect();
        int amp = effect.getAmplifier();
        if(ForbiddenConfig.getInstance().isEffectForbidden(effect1.value(), amp)){
            // 数据包配置文件
            return false;
        }
        if(effect1.getKey() != null && ForbiddenConfig.getInstance().isModForbidden(effect1.getKey().location().getNamespace())) {
            return false;
        }
//        if(ForbiddenConfig.getInstance().isModForbidden(effect1.getKey().location().getNamespace()))
        if(effect1.value().getCategory() == MobEffectCategory.HARMFUL){
            // 负面效果不应该应用
            return false;
        }
        return true;
    };
    /**
     * 物品过滤器
     */
    public static List<Pair<Holder<MobEffect>, Integer>> getApplyEffect(ItemStack stack, boolean ignoreCount){
        Item item = stack.getItem();

        List<Pair<Holder<MobEffect>, Integer>> effects = new ArrayList<>();
        if(ForbiddenConfig.getInstance().isItemForbidden(item)){
            // 数据包配置文件
            return effects;
        }
        if(!ignoreCount && stack.getCount() < CommonConfig.AUTO_POTION_STACK_SIZE.get()) {
            // 配置文件
            return effects;
        }
        if(ConfluenceHelper.isLoaded() && item instanceof EffectPotionItem potion) {
            // 效果类药水
            if(canApplyEffect(new MobEffectInstance(potion.mobEffect, potion.duration, potion.amplifier))){
                effects.add(new Pair<>(potion.mobEffect, potion.amplifier));
            }
            return effects;
        }
        if(item instanceof Item food) {
            //食物类
            var foodProperties = food.getFoodProperties(stack, null);
            if (foodProperties != null) {
                for (FoodProperties.PossibleEffect foodproperties$possibleeffect : foodProperties.effects()) {
                    var mobEffect = foodproperties$possibleeffect.effect();
                    if (canApplyEffect(mobEffect)) {
                        effects.add(new Pair<>(mobEffect.getEffect(), mobEffect.getAmplifier()));
                    }
                }
            }
        }
        if(item instanceof PotionItem potionItem){
            var data = stack.get(DataComponents.POTION_CONTENTS);
            if(data != null){
                data.potion().ifPresent(potion -> {
                    potion.value().getEffects().forEach(effect -> {
                        if(canApplyEffect(effect)){
                            effects.add(new Pair<>(effect.getEffect(), effect.getAmplifier()));
                        }
                    });
                });
            }
        }
        return effects;
    };

    public static List<Pair<Holder<MobEffect>, Integer>> getApplyEffect(ItemStack stack) {
        return getApplyEffect(stack, false);
    }

    public static boolean canApply(ItemStack stack){
        return !getApplyEffect(stack).isEmpty();
    }

    private static PlayerInventoryManager instance;

    public static PlayerInventoryManager getInstance(){
        if(instance == null)
            instance = new PlayerInventoryManager();
        return instance;
    }

    // 采用队列限制每 tick 处理的物品数量
    Queue<ItemStack> consumerQueue = new LinkedList<>();
    List<Pair<Holder<MobEffect>, Integer>> effects = new ArrayList<>();

    int maxHandleItemPerTick = 2;

    private void addAllItems(Collection<ItemStack> consumerQueue, Player player){
        // 背包的药水
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            try {
                ItemStack stack = inventory.getItem(i);
                var data1 = stack.get(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT);
                if(data1 == null){
                    consumerQueue.add(stack);
                }else {
                    // 药水袋
                    consumerQueue.addAll(data1.getItems());
                }
            } catch (Exception ignored) {}
        }
        // 末影箱的药水
        addTargetItemStackList(player.getData(ModAttachments.ENDER_CHEST).getItems(), consumerQueue);

        // 存钱罐
        addTargetItemStackList(player.getData(ModAttachments.PIG_CHEST.get()).getItems(), consumerQueue);

        // 保险箱
        addTargetItemStackList(player.getData(ModAttachments.PIG_CHEST.get()).getItems(), consumerQueue);

    }

    /**
     * 客户端 检测可以作用的容器
     */
    public void detect(Player player){
        // 检测间隔

        if(!player.level().isClientSide()) {
            // 服务端检测
            if(--detectInternal > 0){
                return;
            }
            detectInternal = (int) (_detectInternal * 0.1f);
            this.detectServer(player);
            return;
        }
//        if(--detectInternal > 0){
//            return;
//        }
//        detectInternal = 5;

        if(!serverOpenAutoPotion){
            return;
        }

        if(CommonConfig.AUTO_POTION_OPEN.get()) { // 客户端可自行选择是否启用
            if(consumerQueue.isEmpty()){
                // 扫描数据发送到服务器, 清空数据，开始下一轮检测
                AutoPotionAttachment data = player.getData(ModAttachments.AUTO_POTION);
                data.getPotions().clear();
                // 重新生成缓存
                effects.forEach(effect_amp -> {
                    data.addPotion(effect_amp.getA(), effect_amp.getB());
                });
                data.sync();
                addAllItems(consumerQueue, player);
                effects.clear();
            }
            maxHandleItemPerTick = CommonConfig.AUTO_POTION_SCAN_ITEM_PER_TICK.get();
            for(int i = 0; i < maxHandleItemPerTick && !consumerQueue.isEmpty(); i++){
                ItemStack stack = consumerQueue.poll();
                effects.addAll(getApplyEffect(stack));
            }
        }
    }



    // 这里自动存钱和存放药水等
    private void detectServer(Player player){
        NonNullList<ItemStack> items = player.getInventory().items;
        boolean autoSave = false;
        List<ItemStack> potionBags = new ArrayList<>();
        for(ItemStack stack : items){
            if(ConfluenceHelper.isLoaded() &&  CommonConfig.AUTO_SAVE_MONEY.get() && !autoSave && stack.is(FunctionalBlocks.PIGGY_BANK.asItem())){
               autoSave = true;
            }
            if(stack.getItem() == ModItems.PotionBag.get()){
                var data = stack.get(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT);
                if(data != null && data.isAutoCollect()) {
                    potionBags.add(stack);
                }
            }
        }
        if(autoSave){
            for(ItemStack stack : items){
                if(stack.is(ModTags.Items.COINS)){
                   ModUtils.tryPlaceBackItemStackToItemStacks(stack, player.getData(ModAttachmentTypes.PIGGY_BANK.get()).getItems());
                }
            }
            var data = player.getData(ModAttachmentTypes.EXTRA_INVENTORY.get());

            for(int i = 0; i < 4; i++){
                ItemStack stack = data.getCoins(i);
                if(!stack.isEmpty()){
                    ModUtils.tryPlaceBackItemStackToItemStacks(stack, player.getData(ModAttachmentTypes.PIGGY_BANK.get()).getItems());
                }
            }

        }
        if(player.containerMenu instanceof PotionBagMenu){
            return;
        }
        for(ItemStack stack : items){
            if(PotionBagMenu.canPlace(stack)) {
                for (ItemStack potionBag : potionBags) {
                    ItemContainerComponent data = potionBag.get(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT);
                    if(data == null) continue;
                    List<ItemStack> items1 =  data.getItems();
                    if(ModUtils.tryPlaceBackItemStackToItemStacks(stack, items1)){
                        potionBag.set(ModDataComponentTypes.ITEM_CONTAINER_COMPONENT, new ItemContainerComponent(ItemContainerContents.fromItems(items1), data.isAutoCollect(), data.size));
                        break;
                    }
                }
            }
        }
    }

    /**
     * 添加扫描列表
     * @param items 列表
     * @param to 应用于
     */
    private void addApplyItemList(List<Item> items, List<Pair<Holder<MobEffect>, Integer>> to){
        for (Item item : items) {
            try {
                ItemStack stack = new ItemStack(item, CommonConfig.AUTO_POTION_STACK_SIZE.get());
                to.addAll(getApplyEffect(stack));
            } catch (Exception ignored) {

            }
        }
    }
    private void addTargetItemStackList(List<Item> items, Collection<ItemStack> to){
        for (Item item : items) {
            try {
                ItemStack stack = new ItemStack(item, CommonConfig.AUTO_POTION_STACK_SIZE.get());
                to.add(stack);
            } catch (Exception ignored) { }
        }
    }

    /**
     * 服务端 应用效果
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
    public static void renderApply(AbstractContainerScreen screen, @Nullable Container container, ItemStack stack, GuiGraphics guiGraphics, int x, int y, float partialTick){

        String title = screen.getTitle().toString();
        if((
                ConfluenceHelper.isLoaded() && container instanceof PlayerContainer<?> ||  // 猪猪存钱罐和保险箱
                screen instanceof InventoryScreen || // 背包
                screen instanceof CreativeModeInventoryScreen || // 创造栏
                screen instanceof PotionBagScreen || // 药水袋
                screen instanceof ContainerScreen && (title.contains("enderchest") || title.contains("piggy_bank") || title.contains("safe")) ||
                        ConfluenceHelper.isLoaded() && screen instanceof ExtraInventoryScreen||  // 额外栏
                        CuriosHelper.isLoaded() && screen instanceof CuriosScreen  // 饰品栏
        )
                && canApply(stack)){

            guiGraphics.fillGradient(RenderType.guiOverlay(), x, y, x + 16, y + 16, 0x00200800, 0xf020FFF0, 0);

        }
    }

}
