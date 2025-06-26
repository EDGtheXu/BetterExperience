package com.github.edg_thexu.better_experience.client.buffer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
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
        BufferBuilder buffer = tessellator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        this.buildBuffer(buffer);
        MeshData build = buffer.build();
        if (build == null) {
            this.vertexBuffer = null;
        } else {
            this.vertexBuffer.bind();
            this.vertexBuffer.upload(build);
            VertexBuffer.unbind();
        }

    }

    public void render(RenderLevelStageEvent event) {
        this.render(event.getPoseStack(), event.getModelViewMatrix(), Minecraft.getInstance().gameRenderer.getMainCamera().getPosition(), event.getProjectionMatrix());
    }

    public void render(PoseStack poseStack, Matrix4f modelMatrix, Vec3 playerPos, Matrix4f projectMatrix) {
        if (this.shouldRefresh()) {
            this.refresh();
        }

        if (this.vertexBuffer != null) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            this.beforeRender();
            poseStack.pushPose();
            poseStack.mulPose(modelMatrix);
            poseStack.translate(-playerPos.x(), -playerPos.y(), -playerPos.z());
            this.vertexBuffer.bind();
            this.vertexBuffer.drawWithShader(poseStack.last().pose(), projectMatrix, RenderSystem.getShader());
            VertexBuffer.unbind();
            poseStack.popPose();
            this.afterRender(poseStack);
        }

    }
}
