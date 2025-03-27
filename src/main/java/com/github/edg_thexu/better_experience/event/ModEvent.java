package com.github.edg_thexu.better_experience.event;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.config.CommonConfig;
import com.github.edg_thexu.better_experience.module.autopotion.PlayerAttribute;
import com.github.edg_thexu.better_experience.network.C2S.BreakBlocksPacketC2S;
import com.github.edg_thexu.better_experience.network.C2S.PotionApplyPacketC2S;
import com.github.edg_thexu.better_experience.network.C2S.ServerBoundPacketC2S;
import com.github.edg_thexu.better_experience.network.S2C.EnderChestItemsS2C;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.confluence.mod.Confluence;
import org.confluence.mod.util.PrefixUtils;

import java.util.Map;

@EventBusSubscriber(modid = Better_experience.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvent {
    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(PotionApplyPacketC2S.TYPE, PotionApplyPacketC2S.STREAM_CODEC, PotionApplyPacketC2S::handle);
        registrar.playToServer(BreakBlocksPacketC2S.TYPE, BreakBlocksPacketC2S.STREAM_CODEC, BreakBlocksPacketC2S::handle);
        registrar.playToServer(ServerBoundPacketC2S.TYPE, ServerBoundPacketC2S.STREAM_CODEC, ServerBoundPacketC2S::handle);

        registrar.playToClient(EnderChestItemsS2C.TYPE, EnderChestItemsS2C.STREAM_CODEC, EnderChestItemsS2C::handle);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == CommonConfig.SPEC) {
            // 重新加载人物属性
            PlayerAttribute.dirty = true;
        }
    }

    @SubscribeEvent
    public static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        try {

            if (CommonConfig.MODIFY_MAX_STACK_SIZE.get()) {
                for (Map.Entry<ResourceKey<Item>, Item> entry : BuiltInRegistries.ITEM.entrySet()) {
                    Item item = entry.getValue();
                    DataComponentMap components = item.components();
                    if (components.has(DataComponents.MAX_DAMAGE) || PrefixUtils.couldReforge(item.getDefaultInstance()))
                        continue;
                    String namespace = entry.getKey().location().getNamespace();
                    if ("minecraft".equals(namespace) || Confluence.MODID.equals(namespace)) {
                        int maxStackSize = item.getDefaultMaxStackSize();
                        if (maxStackSize == 1 || components.has(DataComponents.FOOD) || components.has(DataComponents.POTION_CONTENTS)) {
                            event.modify(item, builder -> builder.set(DataComponents.MAX_STACK_SIZE, 99));
                        } else if (maxStackSize == 16) {
                            event.modify(item, builder -> builder.set(DataComponents.MAX_STACK_SIZE, 999));
                        } else if (maxStackSize == 64) {
                            event.modify(item, builder -> builder.set(DataComponents.MAX_STACK_SIZE, 9999));
                        }
                    }
                }
            }
        } catch (IllegalStateException ignored) {

        }
    }
}
