package com.xsm.netty.learn.codec.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author xsm
 * @Date 2020/5/27 22:30
 * 扩展 ByteToMessageDecoder 类的解码器
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() >= 4) { // 检查是否至少有4个字节可读
            // 从入站ByteBuf中读取一个int, 并将其添加到解码消息的List中
            out.add(in.readInt());
        }
    }
}
