package com.xsm.netty.learn.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author: xsm
 * @create: 2020-05-29
 * @description: 处理文本帧
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    /**
     * 处理自定义事件
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
            // 如果该事件表示握手成功, 则从该ChannelPipeline中移除HttpRequestHandler, 因为将不会接受到任何HTTP消息了
            ctx.pipeline().remove(HttpRequestHandler.class);
            // 通知所有已经练级额的WebSocket客户端新的客户端已经连接上了
            group.writeAndFlush(new TextWebSocketFrame("Client " + ctx.channel() + "joined"));
            // 将新的WebSocket Channel添加到ChannelGroup中, 以便它可以接受到所有的消息
            group.add(ctx.channel());
        }
        else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 增加消息的引用计数, 并将它写到ChannelGroup中所有已经连接的客户端
        group.writeAndFlush(textWebSocketFrame.retain());
    }
}
