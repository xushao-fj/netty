package com.xsm.netty.learn.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author xsm
 * @Date 2020/5/27 22:33
 */
public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // in 传入的ByteBuf是ReplayingDecoderByteBuf
        // 从入站ByteBuf中读取一个int, 并将其添加到解码消息的List中
        out.add(in.readInt());
    }
}
