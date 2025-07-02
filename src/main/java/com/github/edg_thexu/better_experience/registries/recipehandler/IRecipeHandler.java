package com.github.edg_thexu.better_experience.registries.recipehandler;

import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.recipehandler.visitor.IRecipeHandlerVisitor;
import com.mojang.serialization.Codec;
import mezz.jei.library.gui.recipes.RecipeLayout;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

/**
 * 配方处理器
 * @param <V> 遍历的类型
 */
public interface IRecipeHandler<V> {


    /**
     * 初始化时使用，但是工厂方法可以平替
     */
    @OnlyIn(Dist.CLIENT)
    default void init(RecipeLayout layout, @Nullable Object recipe){

    }

    /**
     * 生成每个原料的迭代器
     */
    List<V> getIngredient(Level level);

    /**
     * 遍历迭代器的访问者
     */
    IRecipeHandlerVisitor<V> getVisitor();

    /**
     * 以后可能有用
     */
    boolean match(ItemStack stack, int slot);

    /**
     * 获取制作数量
     */
    int count();

    /**
     * 获取编解码器
     * @return 编解码器
     */
    RecipeHandlerProvider getCodec();



//    StreamCodec<RegistryFriendlyByteBuf, IRecipeHandler> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(TYPED_CODEC);

}
