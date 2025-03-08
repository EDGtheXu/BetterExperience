package com.github.edg_thexu.better_experience.client.buffer;

import com.github.edg_thexu.better_experience.client.RenderUtil;
import com.github.edg_thexu.better_experience.config.ClientConfig;
import com.github.edg_thexu.better_experience.item.MagicBoomStaff;
import com.github.edg_thexu.better_experience.utils.ModUtils;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.mod.client.effect.AbstractBufferManager;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class AABBBuffer extends AbstractBufferManager {

    static AABBBuffer instance;

    public static AABBBuffer getInstance() {
        if (instance == null) {
            instance = new AABBBuffer(100);
        }
        return instance;
    }

    AABBBuffer(int refreshTime) {
        super(refreshTime);
    }



    @Override
    protected void beforeRender() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

    }

    @Override
    protected void afterRender(PoseStack poseStack) {
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    protected void buildBuffer(BufferBuilder bufferBuilder) {
        if(!ClientConfig.SHOW_OUTLINES.get()) return;
        Player player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();
        int r;
        BlockPos targetPos;
        if(stack.getItem() instanceof MagicBoomStaff staff){
            r = staff.range;
            targetPos = ModUtils.getEyeBlockHitResult(Minecraft.getInstance().player, staff.maxRange * 2);
        }else return;

        int x = r;
        int y = r;
        int z = r;

//            RenderSystem.setShaderTexture(0, Rhyme.space("textures/block/card_up_level_block.png"));
        float random = System.currentTimeMillis() % 1000000 * 0.00008f;
        double offsetU = Math.sin(random) * 1f + random * 2;
        double offsetV = Math.cos(random) * 1f  + Math.cos(random * 0.3f) * 0.01f;

        RenderUtil.renderAABBOutLine(bufferBuilder,
                targetPos.getX() -x, targetPos.getY() -y, targetPos.getZ()-z,targetPos.getX() + x+1, targetPos.getY() + y+1, targetPos.getZ() + z+1,
                255,125,128,255,
                1
        );
    }
}
