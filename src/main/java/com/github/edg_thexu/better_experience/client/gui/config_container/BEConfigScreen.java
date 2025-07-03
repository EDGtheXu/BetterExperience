package com.github.edg_thexu.better_experience.client.gui.config_container;

import com.github.edg_thexu.better_experience.config.ConfigRegistry;
import com.github.edg_thexu.better_experience.intergration.terra_entity.TEHelper;
import com.github.edg_thexu.cafelib.client.RenderUtil;
import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreen;
import com.github.edg_thexu.cafelib.client.gui.config_container.ConfigScreenBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.client.ModRenderTypes;
import org.confluence.terraentity.mixed.IShaderInstance;

import java.util.function.Function;


@OnlyIn(Dist.CLIENT)
public class BEConfigScreen extends ConfigScreen {

    public BEConfigScreen(Screen screen, Function<ConfigScreen, ConfigScreenBuilder> builderFunction) {
        super(screen, Component.translatable("better_experience.options.title"), builderFunction);
    }


    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        super.render(g, mouseX, mouseY, partialTicks);

         //渲染彩色标题

        if(TEHelper.isLoaded()) {
            target.setClearColor(0, 0, 0, 0);
            target.clear(false);
            target.bindWrite(true);

            g.pose().pushPose();
            g.pose().translate(0, -getScrollAmount(), 0);
            g.drawCenteredString(this.font, this.title, this.width / 2, 15, 0x12a2c6);
            g.pose().popPose();

            minecraft.getMainRenderTarget().bindWrite(true);
            target.blitToScreen(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight(), false);


            RenderUtil.blitScreen(ModRenderTypes.Shaders.floatBarShader, shader -> {
                // 流动速度
                float speed = 0.005f;
                ((IShaderInstance) shader).getTerra_entity$Time().set(System.currentTimeMillis() % 100000 * speed);
                // 噪声强度
                ((IShaderInstance) shader).getTerra_entity$Radius().set(0.5f);
                shader.COLOR_MODULATOR.set(0.5f, 0.6f, 1f, 1f);
                shader.setSampler("Sampler0", target);
                shader.setSampler("Sampler1", Minecraft.getInstance().getTextureManager().getTexture(TerraEntity.space("textures/gui/noise.png")));
            });
        }else{
            g.drawCenteredString(this.font, this.title, this.width / 2, (int) (15 - getScrollAmount()), 0x12a2c6);
        }
    }


    @Override
    protected ForgeConfigSpec getSpec() {
        return ConfigRegistry.SPEC;
    }
}
