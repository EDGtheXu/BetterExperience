package com.github.edg_thexu.better_experience.module.reforge;

import com.github.edg_thexu.better_experience.client.gui.widget.FloatButton;
import com.github.edg_thexu.better_experience.init.ModAttachments;
import com.github.edg_thexu.better_experience.mixin.accessor.ScreenAccessor;
import com.github.edg_thexu.better_experience.networks.c2s.ServerBoundPacketC2S;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.confluence.mod.client.gui.container.NPCReforgeScreen;
import org.confluence.mod.common.component.prefix.ModPrefix;
import org.confluence.mod.common.component.prefix.PrefixComponent;
import org.confluence.mod.common.component.prefix.PrefixType;
import org.confluence.mod.common.init.ModDataComponentTypes;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BetterReforgeManager {

    public static int getBetterPrefix(PrefixType prefixType, ItemStack itemStack){

        var available = prefixType.getAvailable();
        PrefixComponent cp = itemStack.get(ModDataComponentTypes.PREFIX);
        float originalValue = cp.value();
        List<Integer> availableList = new ArrayList<>();
        for(int i = 1; i < available.length; i++){
            int ii = ModPrefix.ID_MAP.inverse().getOrDefault(available[i], -1);
            ModPrefix modPrefix = ModPrefix.ID_MAP.get(ii);
            float value =modPrefix.createComponent(prefixType).value();
            if(value >= originalValue){
                availableList.add(ii);
            }
        }
        Collections.shuffle(availableList);
        return availableList.getFirst();
    }


    public static void initButton(NPCReforgeScreen screen){


    }

    public static void init(NPCReforgeScreen screen){
        FloatButton button = new FloatButton((FloatButton.Builder) FloatButton.builder(Component.literal("B"), (p)->{
            AdapterUtils.sendToServer(new ServerBoundPacketC2S(6));
        }).tooltip(Tooltip.create(Component.translatable("better_experience.tooltip.better_reforge.enable"))).bounds(screen.width / 2 - 88, screen.height / 2 - 30, 30, 15));
        ((ScreenAccessor)screen).callAddRenderableWidget(button);
        button.setSelectedDirectly(Minecraft.getInstance().player.getData(ModAttachments.TEMP_DATA).enableBetterReforge());

    }
}
