package com.github.edg_thexu.better_experience.client.renderer;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.block.AutoFishBlock;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class AutoFishBlockRenderer extends GeoBlockRenderer<AutoFishBlock.AutoFishMachineEntity> {
    public AutoFishBlockRenderer() {
        super(new DefaultedBlockGeoModel<>(Better_experience.space("auto_fish_block")));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this){
            @Override
            public void render(PoseStack poseStack, AutoFishBlock.AutoFishMachineEntity animatable, BakedGeoModel bakedModel, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

                super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            }

            @Override
            protected ResourceLocation getTextureResource(AutoFishBlock.AutoFishMachineEntity animatable) {
                return Better_experience.space("textures/block/auto_fish_block_glow.png");
            }

            @Override
            protected RenderType getRenderType(AutoFishBlock.AutoFishMachineEntity animatable) {
                return RenderType.entityTranslucentEmissive(getTextureResource(animatable));
            }
        });
    }

//    @Override
//    public AABB getRenderBoundingBox(AutoFishBlock.AutoFishMachineEntity blockEntity) {
//        return super.getRenderBoundingBox(blockEntity).inflate(0,5,0);
////        return AABB.INFINITE;
//    }
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(Better_experience.space("textures/entity/fishing_hook.png"));

    public void actuallyRender(PoseStack poseStack, AutoFishBlock.AutoFishMachineEntity animatable, BakedGeoModel model, @Nullable RenderType renderType,
                                MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick,
                                int packedLight, int packedOverlay, float r, float g, float b, float a) {

        if(isReRender) super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, r,g,b,a);
    }

    @Override
    public void defaultRender(PoseStack poseStack, AutoFishBlock.AutoFishMachineEntity entity, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer,
                              float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, entity, bufferSource, renderType, buffer, yaw, partialTick, packedLight);

        poseStack.pushPose();
        int time = entity.fishingTime;
        int timeRemain = entity.cacheTime;
        float len = ModUtils.clamp(
                Math.min(time - timeRemain + partialTick, timeRemain - partialTick) / 20f,
                0, 2);

        poseStack.translate(0.5, -len, 0.5);

        poseStack.pushPose();

        poseStack.scale(0.5F, 0.5F, 0.5F);

        poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        PoseStack.Pose posestack$pose = poseStack.last();

        VertexConsumer vertexconsumer = bufferSource.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, posestack$pose, packedLight, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, posestack$pose, packedLight, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, posestack$pose, packedLight, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, posestack$pose, packedLight, 0.0F, 1, 0, 0);
        poseStack.popPose();

        VertexConsumer vertexconsumer1 = bufferSource.getBuffer(RenderType.lineStrip());
        PoseStack.Pose posestack$pose1 = poseStack.last();


        for(int j = 0; j <= 16; ++j) {
            stringVertex(0, len, 0, vertexconsumer1, posestack$pose1, fraction(j, 16), fraction(j + 1, 16));
        }

        poseStack.popPose();


    }



    private static float fraction(int numerator, int denominator) {
        return (float)numerator / (float)denominator;
    }

    public void reRender(BakedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, AutoFishBlock.AutoFishMachineEntity animatable,
                         RenderType renderType, VertexConsumer buffer, float partialTick,
                         int packedLight, int packedOverlay, float r, float g, float b, float a) {

        if(animatable.getState() == 0) return;
        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(1.1f, 1.1f, 1.1f);
        super.reRender(model, poseStack, bufferSource, animatable, renderType, buffer, partialTick, packedLight, packedOverlay, r, g, b, a);
    }


    private static void vertex(VertexConsumer consumer, PoseStack.Pose pose, int packedLight, float x, int y, int u, int v) {
        consumer.vertex(pose.pose(), x - 0.5F, (float)y - 0.5F, 0.0F).color(-1).uv((float)u, (float)v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(pose.normal(), 0.0F, 1.0F, 0.0F);
    }

    private static void stringVertex(float x, float y, float z, VertexConsumer consumer, PoseStack.Pose pose, float stringFraction, float nextStringFraction) {
        float f = x * stringFraction;
        float f1 = y * (stringFraction * stringFraction + stringFraction) * 0.5F + 0.25F;
        float f2 = z * stringFraction;
        float f3 = x * nextStringFraction - f;
        float f4 = y * (nextStringFraction * nextStringFraction + nextStringFraction) * 0.5F + 0.25F - f1;
        float f5 = z * nextStringFraction - f2;
        float f6 = Mth.sqrt(f3 * f3 + f4 * f4 + f5 * f5);
        f3 /= f6;
        f4 /= f6;
        f5 /= f6;
        consumer.vertex(pose.pose(), f, f1, f2).color(-16777216).normal(pose.normal(), f3, f4, f5);
    }

}
