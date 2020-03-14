package com.xsm.netty.thirdexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author xsm
 * @Date 2020/3/14 21:23
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * channel 组, 保存channel
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        // 判断是否是自己发送的数据
        CHANNEL_GROUP.forEach(ch -> {
            if (channel != ch) {
                // 当前所遍历的对象不是发消息的客户端, 需要发送消息
                ch.writeAndFlush(channel.remoteAddress() + "发送消息: " + msg);
            } else {
                // 当前自己链接发送消息
                ch.writeAndFlush("[自己]" + msg + "\n");
            }
        });
    }

    /**
     * 链接建立
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取链接对象
        Channel channel = ctx.channel();

        // 发送给其他链接的客户端说明该客户端已链接
        CHANNEL_GROUP.writeAndFlush("[服务器] - " + channel.remoteAddress() + "加入\n");

        // 保存新建立的链接
        CHANNEL_GROUP.add(channel);

    }

    /**
     * 链接断开
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        CHANNEL_GROUP.writeAndFlush("[服务器] - " + channel.remoteAddress() + "离开\n");
        // 移除链接, 其实netty会自动调用, 无需编写
        CHANNEL_GROUP.remove(channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "下线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
