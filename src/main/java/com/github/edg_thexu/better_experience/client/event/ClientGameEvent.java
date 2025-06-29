package com.github.edg_thexu.better_experience.client.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.client.buffer.AABBBuffer;
import com.github.edg_thexu.better_experience.config.ClientConfig;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import com.github.edg_thexu.better_experience.intergration.confluence.ConfluenceHelper;
import com.github.edg_thexu.better_experience.item.MagicBoomStaff;
import com.github.edg_thexu.better_experience.networks.c2s.ServerBoundPacketC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.confluence.mod.client.gui.container.ExtraInventoryScreen;
import org.confluence.mod.common.item.hammer.HammerItem;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.GAME,value = Dist.CLIENT)
public class ClientGameEvent {

    @SubscribeEvent
    public static void renderLevelStage(RenderLevelStageEvent event) {
//        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
//            //PostUtil.postProcess();
//
//        }
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
            AABBBuffer.getInstance().render(event);

        }
    }

    @SubscribeEvent
    public static void event(InputEvent.MouseScrollingEvent event){

        if(Minecraft.getInstance().player!=null && Minecraft.getInstance().player.input.shiftKeyDown){
            ItemStack stack = Minecraft.getInstance().player.getMainHandItem();
            if(stack.getItem() instanceof MagicBoomStaff staff){
                staff.range = Math.clamp( staff.range + (event.getScrollDeltaY() > 0 ? 1 : -1), 1, staff.maxRange);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void event(InputEvent.MouseButton.Pre event){
        if(!ConfluenceHelper.isLoaded()){
            return;
        }
        if (Minecraft.getInstance().player != null && ClientConfig.MULTI_FISHING.get() &&  event.getButton() == 0 && event.getAction() == 1 && Minecraft.getInstance().player.getMainHandItem().getItem() instanceof HammerItem) {
            PacketDistributor.sendToServer(new ServerBoundPacketC2S(3));

        }

    }

    @SubscribeEvent
    public static void closeScreen(ScreenEvent.Closing event){
        if(ConfluenceHelper.isLoaded() && event.getScreen() instanceof ExtraInventoryScreen){
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.getData(ModAttachments.AUTO_POTION).sync(true);
            }
        }
    }
}
