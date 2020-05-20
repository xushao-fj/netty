package com.xsm.netty.learn.firstdemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author xsm
 * @Date 2020/5/19 22:29
 */
@ChannelHandler.Sharable // 表示一个ChannelHandler可以被多个Channel安全的共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 对于每个传入的消息都要调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("server received: " + in.toString(CharsetUtil.UTF_8));
        // 将接收到的消息写给发送者, 而不冲刷出站消息
        ctx.write(in);
    }

    // 通知 ChannelInboundHandler最后一次对channelRead()方法的调用时当前批量读取中的最后一条消息
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将未决消息冲刷到远程节点, 并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    // 在读取操作期间, 有异常抛出时会调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
