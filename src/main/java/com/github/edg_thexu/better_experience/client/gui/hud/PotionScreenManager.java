package com.github.edg_thexu.better_experience.client.gui.hud;

import com.github.edg_thexu.better_experience.init.ModAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.confluence.mod.client.gui.container.ExtraInventoryScreen;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PotionScreenManager {

    private static final ResourceLocation EFFECT_BACKGROUND_SMALL_SPRITE = ResourceLocation.withDefaultNamespace("container/inventory/effect_background_small");

    ExtraInventoryScreen screen;
    int leftPos;
    int topPos;
    int imageWidth;
    int imageHeight;
    Minecraft minecraft;
    Player player;
    int width;
    Font font;
    MobEffectInstance selectedEffect = null;

    static PotionScreenManager instance;

    public PotionScreenManager(ExtraInventoryScreen screen){
        this.screen = screen;
        this.imageWidth = 0;
        this.imageHeight = 0;
        this.minecraft = Minecraft.getInstance();
        this.player = this.minecraft.player;
        this.width = screen.getXSize();
        this.font = Minecraft.getInstance().font;
        instance = this;
    }

    public static PotionScreenManager getInstance(){
        return instance;
    }

    public void render(GuiGraphics g, int mousex,int mousey, float partialTicks){
        this.leftPos = screen.getGuiLeft();
        this.topPos = screen.getGuiTop();

        renderEffects(g, mousex, mousey);
    }

    public void click(double mousex, double mousey, int button){
        if(selectedEffect!= null){
            var data = player.getData(ModAttachments.AUTO_POTION.get());
            if(data.isForbidden(selectedEffect.getEffect())){
                data.removeForbidden(selectedEffect.getEffect());
            } else data.addForbidden(selectedEffect.getEffect());
//            data.sync();
        }
    }

    private void renderEffects(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int i = this.leftPos - 20;
        int j = this.width - i;
        var data = player.getData(ModAttachments.AUTO_POTION.get());

        Collection<MobEffectInstance> collection = data.getPotions().entrySet().stream().map(entry -> new MobEffectInstance(entry.getKey(), -1,entry.getValue())).collect(Collectors.toSet());
        if (!collection.isEmpty()) {
            int k = 12;

            i = i + k;
            Iterable<MobEffectInstance> iterable = collection.stream().sorted().collect(Collectors.toList());
            int jj = this.topPos - k;
            int ii = i;
            int maxCount = 15;
            int count = 0;

            MobEffectTextureManager mobeffecttexturemanager = this.minecraft.getMobEffectTextures();
            MobEffectInstance selectedEffect = null;

            for (MobEffectInstance mobeffectinstance : iterable) {
                if(data.isForbidden(mobeffectinstance.getEffect())){
                    guiGraphics.setColor(0.3f, 0.3f, 0.3f, 1.0f);
                }

                guiGraphics.blitSprite(EFFECT_BACKGROUND_SMALL_SPRITE, ii, jj, 12, 12);
                Holder<MobEffect> holder = mobeffectinstance.getEffect();
                TextureAtlasSprite textureatlassprite = mobeffecttexturemanager.get(holder);
                guiGraphics.blit(ii + 1 , jj +1 , 0, 10, 10, textureatlassprite);

                if(selectedEffect == null && mouseX >= ii && mouseX <= ii + k && mouseY >= jj && mouseY <= jj + k){
                    selectedEffect = mobeffectinstance;
                }

                ii += k;
                count++;
                if(count >= maxCount){
                    jj -= k + 2;
                    ii = i;
                    count = 0;
                }

                guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            }

            if (selectedEffect != null) {
                List<Component> list = List.of(
                        this.getEffectName(selectedEffect),
                        MobEffectUtil.formatDuration(selectedEffect, 1.0F, this.minecraft.level.tickRateManager().tickrate())
                );
                guiGraphics.renderTooltip(this.font, list, Optional.empty(), mouseX, mouseY);
            }
            this.selectedEffect = selectedEffect;

        }
    }

    private Component getEffectName(MobEffectInstance effect) {
        MutableComponent mutablecomponent = effect.getEffect().value().getDisplayName().copy();
        if (effect.getAmplifier() >= 1 && effect.getAmplifier() <= 9) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + (effect.getAmplifier() + 1)));
        }

        return mutablecomponent;
    }
}
