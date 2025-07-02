package com.github.edg_thexu.better_experience.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;

import java.util.Iterator;
import java.util.List;

public class RenderUtil {
    public static void renderDebugBlock(VertexConsumer buffer, BlockPos pos, float size, int r, int g, int b, int a){
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        buffer.vertex(x, y + size, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z).color(r,g,b,a).endVertex();

        // BOTTvertex()
        buffer.vertex(x + size, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y, z).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x + size, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z + size).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x + size, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x + size, y + size, z).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x, y, z + size).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z + size).color(r,g,b,a).endVertex();

        // Edgevertex()
        buffer.vertex(x, y, z).color(r,g,b,a).endVertex();
        buffer.vertex(x, y + size, z).color(r,g,b,a).endVertex();
    }

    public static void renderAABB(VertexConsumer buffer,
                                  float x1, float y1, float z1, float x2, float y2, float z2,
                                  int r, int g, int b, int a,
                                  float size, float u, float v

    ){
        float w = x2 - x1;
        float h = y2 - y1;
        float d = z2 - z1;

        float scaleX =  w / size;
        float scaleY =  h / size;
        float scaleZ =  d / size;

        buffer.vertex(x1, y1, z1).color(r,g,b,a).uv(0 + u, 0+v).endVertex();
        buffer.vertex(x2, y1, z1).color(r,g,b,a).uv(scaleX+ u, 0+v).endVertex();
        buffer.vertex(x2, y2, z1).color(r,g,b,a).uv(scaleX+ u, scaleY+v).endVertex();
        buffer.vertex(x1, y2, z1).color(r,g,b,a).uv(0 + u, scaleY+v).endVertex();

        buffer.vertex(x1, y1, z2).color(r,g,b,a).uv(0 + u, 0+v).endVertex();
        buffer.vertex(x2, y1, z2).color(r,g,b,a).uv(scaleX+ u, 0+v).endVertex();
        buffer.vertex(x2, y2, z2).color(r,g,b,a).uv(scaleX+ u, scaleY+v).endVertex();
        buffer.vertex(x1, y2, z2).color(r,g,b,a).uv(0 + u, scaleY+v).endVertex();

        buffer.vertex(x1, y1, z1).color(r,g,b,a).uv(0+ u, 0+v).endVertex();
        buffer.vertex(x1, y1, z2).color(r,g,b,a).uv(scaleZ+ u, 0+v).endVertex();
        buffer.vertex(x1, y2, z2).color(r,g,b,a).uv(scaleZ+ u, scaleY+v).endVertex();
        buffer.vertex(x1, y2, z1).color(r,g,b,a).uv(0 + u, scaleY+v).endVertex();

        buffer.vertex(x2, y1, z1).color(r,g,b,a).uv(0+ u, 0+v).endVertex();
        buffer.vertex(x2, y1, z2).color(r,g,b,a).uv(scaleZ+ u, 0+v).endVertex();
        buffer.vertex(x2, y2, z2).color(r,g,b,a).uv(scaleZ+ u, scaleY+v).endVertex();
        buffer.vertex(x2, y2, z1).color(r,g,b,a).uv(0 + u, scaleY+v).endVertex();

        buffer.vertex(x1, y1, z1).color(r,g,b,a).uv(0+ u, 0+v).endVertex();
        buffer.vertex(x1, y1, z2).color(r,g,b,a).uv(scaleZ+ u, 0+v).endVertex();
        buffer.vertex(x2, y1, z2).color(r,g,b,a).uv(scaleZ+ u, scaleX+v).endVertex();
        buffer.vertex(x2, y1, z1).color(r,g,b,a).uv(0 + u, scaleX+v).endVertex();

        buffer.vertex(x1, y2, z1).color(r,g,b,a).uv(0+ u, 0+v).endVertex();
        buffer.vertex(x1, y2, z2).color(r,g,b,a).uv(scaleZ+ u, 0+v).endVertex();
        buffer.vertex(x2, y2, z2).color(r,g,b,a).uv(scaleZ+ u, scaleX+v).endVertex();
        buffer.vertex(x2, y2, z1).color(r,g,b,a).uv(0+ u, scaleX+v).endVertex();

    }

    public static void renderAABBOutLine(VertexConsumer buffer,
                                  float x1, float y1, float z1, float x2, float y2, float z2,
                                  int r, int g, int b, int a,
                                  float size

    ){

        buffer.vertex(x1, y1, z1).color(r,g,b,a).endVertex();
//        buffer.vertex(x2, y1, z1).color(r,g,b,a).endVertex();
//        buffer.vertex(x2, y2, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y2, z1).color(r,g,b,a).endVertex();

//        buffer.vertex(x1, y1, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y1, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y2, z2).color(r,g,b,a).endVertex();
//        buffer.vertex(x1, y2, z2).color(r,g,b,a).endVertex();

//        buffer.vertex(x1, y1, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y1, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y2, z2).color(r,g,b,a).endVertex();
//        buffer.vertex(x1, y2, z1).color(r,g,b,a).endVertex();

        buffer.vertex(x2, y1, z1).color(r,g,b,a).endVertex();
//        buffer.vertex(x2, y1, z2).color(r,g,b,a).endVertex();
//        buffer.vertex(x2, y2, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y2, z1).color(r,g,b,a).endVertex();

        buffer.vertex(x1, y1, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y1, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y1, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y1, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y1, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y1, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y1, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y1, z1).color(r,g,b,a).endVertex();

        buffer.vertex(x1, y2, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y2, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y2, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y2, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y2, z2).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y2, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x2, y2, z1).color(r,g,b,a).endVertex();
        buffer.vertex(x1, y2, z1).color(r,g,b,a).endVertex();

    }

    public static void customTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY, List<ClientTooltipComponent> components){
        Minecraft minecraft = Minecraft.getInstance();
//        List<ClientTooltipComponent> components = List.of(
//                ClientTooltipComponent.create(FormattedCharSequence.forward("hello world", Style.EMPTY.withColor(0xFFFFFF)))
////                    ClientTooltipComponent.create(Component.translatable(SwordItems.BEE_KEEPER.get().getDescriptionId()).withStyle(Style.EMPTY.withColor(0xFFFFFF)).getVisualOrderText())
//
//        );
        int i = 0;
        int j = components.size() == 1 ? -2 : 0;

        ClientTooltipComponent clienttooltipcomponent;
        for(Iterator<ClientTooltipComponent> var9 = components.iterator(); var9.hasNext(); j += clienttooltipcomponent.getHeight()) {
            clienttooltipcomponent = var9.next();
            int k = clienttooltipcomponent.getWidth(minecraft.font);
            if (k > i) {
                i = k;
            }
        }

        int l = mouseX + 12;
        int i1 = mouseY - 12;
        guiGraphics.pose().pushPose();

        TooltipRenderUtil.renderTooltipBackground(guiGraphics, l, i1, i, j, 400,
                -267386864, -267386864,
                1347420415, 1347420415);

        guiGraphics.pose().translate(0.0F, 0.0F, 400.0F);
        int k1 = i1;

        int k2;
        ClientTooltipComponent clienttooltipcomponent2;
        for(k2 = 0; k2 < components.size(); ++k2) {
            clienttooltipcomponent2 = components.get(k2);
            clienttooltipcomponent2.renderText(minecraft.font, l, k1, guiGraphics.pose().last().pose(), guiGraphics.bufferSource());
            k1 += clienttooltipcomponent2.getHeight() + (k2 == 0 ? 2 : 0);
        }

        k1 = i1;

        for(k2 = 0; k2 < components.size(); ++k2) {
            clienttooltipcomponent2 = components.get(k2);
            clienttooltipcomponent2.renderImage(minecraft.font, l, k1, guiGraphics);
            k1 += clienttooltipcomponent2.getHeight() + (k2 == 0 ? 2 : 0);
        }

        guiGraphics.pose().popPose();
    }
}
