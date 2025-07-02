package com.github.edg_thexu.better_experience.registries.recipehandler;

import com.github.edg_thexu.better_experience.Better_experience;
import com.github.edg_thexu.better_experience.intergration.jei.JeiRegistries;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.ItemStackUniversalHandler;
import com.github.edg_thexu.better_experience.registries.recipehandler.variant.VanillaRecipeHolderHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;


public class RecipeHandlerProviderTypes {
    public static final DeferredRegister<RecipeHandlerProvider> TYPES = DeferredRegister.create(JeiRegistries.RecipeHandlerProviders.KEY, Better_experience.MODID);
    public static final Supplier<IForgeRegistry<RecipeHandlerProvider>> REGISTRY = TYPES.makeRegistry(RegistryBuilder::new);

    public static final Supplier<RecipeHandlerProvider> VANILLA_TYPE = register("simple_track_type", ()->VanillaRecipeHolderHandler.CODEC);
    public static final Supplier<RecipeHandlerProvider> BREW_TYPE = register("brew_type", ItemStackUniversalHandler.CODEC);


    private static Supplier<RecipeHandlerProvider> register(String name, Supplier<MapCodec<? extends IRecipeHandler>> codec) {
        return TYPES.register(name, ()->new RecipeHandlerProvider(codec));
    }

    static Codec<IRecipeHandler> _TYPED_CODEC = null;

    public static Supplier<Codec<IRecipeHandler>> TYPED_CODEC = ()->{
        try {
            if(_TYPED_CODEC == null){
                _TYPED_CODEC = RecipeHandlerProviderTypes.REGISTRY.get()
                        .getCodec()
                        .dispatch(IRecipeHandler::getCodec, a->a.codec().get().codec());
            }
            return _TYPED_CODEC;
        }catch (Exception e){
            return null;
        }

    };
}
