package com.github.edg_thexu.better_experience.init;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.attachment.AutoPotionAttachment;
import com.github.edg_thexu.better_experience.attachment.AutoPotionAttachmentProvider;
import com.github.edg_thexu.better_experience.attachment.EnderChestAttachment;
import com.github.edg_thexu.better_experience.attachment.EnderChestAttachmentProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModAttachments {
//    public static final DeferredRegister<AttachmentType<?>> TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, Better_experience.MODID);

//    public static final Supplier<AttachmentType<EnderChestAttachment>> ENDER_CHEST = TYPES.register("ender_chest", () -> AttachmentType.serializable(EnderChestAttachment::new).copyOnDeath().build());
//    public static final Supplier<AttachmentType<AutoPotionAttachment>> AUTO_POTION = TYPES.register("auto_potion", () -> AttachmentType.serializable(AutoPotionAttachment::new).copyOnDeath().build());
//    public static final Supplier<AttachmentType<EnderChestAttachment>> PIG_CHEST = TYPES.register("pig_chest", () -> AttachmentType.serializable(EnderChestAttachment::new).copyOnDeath().build());
//    public static final Supplier<AttachmentType<EnderChestAttachment>> SAFE_CHEST = TYPES.register("safe_chest", () -> AttachmentType.serializable(EnderChestAttachment::new).copyOnDeath().build());


    public static final Capability<EnderChestAttachment> ENDER_CHEST = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<AutoPotionAttachment> AUTO_POTION = CapabilityManager.get(new CapabilityToken<>() {});


    @Mod.EventBusSubscriber(modid = Better_experience.MODID,bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class registerCapabilities {
        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            event.register(AutoPotionAttachment.class);
            event.register(EnderChestAttachment.class);
        }
    }

    @Mod.EventBusSubscriber(modid = Better_experience.MODID,bus = Mod.EventBusSubscriber.Bus.FORGE)
    private static class attachCapabilities {
        @SubscribeEvent
        public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof Player){
                event.addCapability(Better_experience.space("summoner_record"), new AutoPotionAttachmentProvider());
                event.addCapability(Better_experience.space("weapon_storage"), new EnderChestAttachmentProvider());
            }
        }
    }

}
