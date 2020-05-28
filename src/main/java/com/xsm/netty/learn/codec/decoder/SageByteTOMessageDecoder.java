package com.xsm.netty.learn.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

import java.util.List;

/**
 * @author xsm
 * @Date 2020/5/27 22:37
 * TooLongFrameException 解码器在帧超出指定的大小限制时抛出
 */
public class SageByteTOMessageDecoder extends ByteToMessageDecoder {
    /** 限制帧的大小*/
    private final static int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int readable = in.readableBytes();
        if (readable > MAX_FRAME_SIZE) {
            // 跳过所有的可读字节
            in.skipBytes(readable);
            // 抛出异常并通知ChannelHandler
            throw new TooLongFrameException("Frame to big!");
        }
        // do something

    }
}
