package com.github.edg_thexu.better_experience.registries.itemmatcher;

import com.mojang.serialization.MapCodec;

public record ItemMatcherProvider(MapCodec<? extends IItemMatcher> codec) {
}
