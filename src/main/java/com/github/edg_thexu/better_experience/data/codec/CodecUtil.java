package com.github.edg_thexu.better_experience.data.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CodecUtil {
    public static Codec<Tag> TAG_CODEC = Codec.of(new Encoder<>() {
        @Override
        public <T> DataResult<T> encode(Tag input, DynamicOps<T> ops, T prefix) {
            var builder = ops.mapBuilder();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(outputStream);
            try {
                NbtIo.writeUnnamedTag(input, output);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            builder.add("tag", ops.createString(outputStream.toString()));
            return builder.build(prefix);
        }
    }, new Decoder<>() {
        @Override
        public <T> DataResult<Pair<Tag, T>> decode(DynamicOps<T> ops, T input) {
            var map = ops.getMap(input).get().left().get();
            var tag = map.get("tag");
            ByteBuf byteBuf;
            if(ops.getStringValue(tag).result().isPresent()){
                byteBuf = Unpooled.copiedBuffer(ops.getStringValue(tag).result().get(), CharsetUtil.UTF_8);
                try {
                    ByteBufInputStream bufInputStream =  new ByteBufInputStream(byteBuf);
                    Tag tag1 = NbtIo.read(bufInputStream);
                    return DataResult.success(Pair.of(tag1, input));
                } catch (IOException e) {
                    return DataResult.error(() -> "No tag found in map:");
                }
            }else{
                return DataResult.error(()->"No tag found in map:");
            }
        }
    });
}
