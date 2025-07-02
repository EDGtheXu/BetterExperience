package com.github.edg_thexu.better_experience.client.buffer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

public abstract class AbstractBufferManager {
    private VertexBuffer vertexBuffer;
    long lastRefreshTime = 0L;
    final int refreshInterval;

    public AbstractBufferManager(int refreshTime) {
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        this.refreshInterval = refreshTime;
    }

    boolean shouldRefresh() {
        return System.currentTimeMillis() - this.lastRefreshTime > (long)this.refreshInterval;
    }

    protected abstract void beforeRender();

    protected abstract void afterRender(PoseStack var1);

    protected abstract void buildBuffer(BufferBuilder var1);

    public void refresh() {
        this.lastRefreshTime = System.currentTimeMillis();
        this.vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder buffer = tessellator.getBuilder();

        beginBuffer(buffer);
        buildBuffer(buffer);

        var build = buffer.end();
        vertexBuffer.bind();
        vertexBuffer.upload(build);
        VertexBuffer.unbind();

    }

    protected void beginBuffer(BufferBuilder buffer){
        buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
    }

    public void render(RenderLevelStageEvent event) {
        render(event.getPoseStack(), Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), event.getProjectionMatrix());
    }

    public void render(PoseStack poseStack, Vec3 playerPos, Matrix4f projectMatrix){
        if(shouldRefresh())
            refresh();

        if (vertexBuffer != null) {

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            beforeRender();

            poseStack.pushPose();
            poseStack.translate(-playerPos.x(), -playerPos.y(), -playerPos.z());

//            minecraft.getBlockRenderer().renderSingleBlock(minecraft.level.getBlockState(BlockPos.containing(playerPos.subtract(0,-1,0))),poseStack,minecraft.renderBuffers().bufferSource(),15, OverlayTexture.NO_OVERLAY);
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(poseStack.last().pose(), projectMatrix, RenderSystem.getShader());
            VertexBuffer.unbind();
            poseStack.popPose();

            afterRender(poseStack);

        }
    }
}
