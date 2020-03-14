package com.xsm.netty.secondexample;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

/**
 * @author xsm
 * @Date 2020/3/14 20:54
 */
public class MyServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // ctx netty上下文, msg客户端所发送的对象
        // 客户端远程地址 + 请求信息
        System.out.println(ctx.channel().remoteAddress() + ", " + msg);
        // 响应, 使用writeAndFlush
        ctx.channel().writeAndFlush("from server: " + UUID.randomUUID());

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 如果出现异常, 关掉链接
        ctx.close();
    }
}
