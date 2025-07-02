package com.github.edg_thexu.better_experience.client.renderer;

import com.github.edg_thexu.better_experience.block.AutoSellBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class AutoSellBlockRenderer implements BlockEntityRenderer<AutoSellBlock.AutoSellBlockEntity> {


    @Override
    public void render(AutoSellBlock.AutoSellBlockEntity autoSellBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {

//        poseStack.pushPose();
//        poseStack.translate(0.5, 0.5, 0);
//        Minecraft.getInstance().getItemRenderer().renderStatic(ModItems.GOLDEN_COIN.toStack(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, autoSellBlockEntity.getLevel(), 0 );
//
//        Quaternionf q = Axis.YP.rotationDegrees(90);
//        poseStack.translate(0.5,0,0.5);
//        poseStack.mulPose(q);
//        Minecraft.getInstance().getItemRenderer().renderStatic(ModItems.GOLDEN_COIN.toStack(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, autoSellBlockEntity.getLevel(), 0 );
//        poseStack.translate(-0.5,0,-0.5);
//        poseStack.mulPose(q);
//        Minecraft.getInstance().getItemRenderer().renderStatic(ModItems.GOLDEN_COIN.toStack(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, autoSellBlockEntity.getLevel(), 0 );
//        poseStack.translate(0.5,0,0.5);
//        poseStack.mulPose(q);
//        Minecraft.getInstance().getItemRenderer().renderStatic(ModItems.GOLDEN_COIN.toStack(), ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, multiBufferSource, autoSellBlockEntity.getLevel(), 0 );
//
//        poseStack.popPose();
    }
}
