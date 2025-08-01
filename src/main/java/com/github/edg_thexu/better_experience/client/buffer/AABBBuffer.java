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
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

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

        RenderUtil.renderAABBOutLine(bufferBuilder,
                targetPos.getX() -x + 0.01f, targetPos.getY() -y + 0.01f, targetPos.getZ()-z + 0.01f,targetPos.getX() + x+1 - 0.01f, targetPos.getY() + y+1 - 0.01f, targetPos.getZ() + z+1 - 0.01f,
                255,125,128,255,
                1
        );
    }
}
