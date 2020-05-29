package com.xsm.netty.learn.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author: xsm
 * @create: 2020-05-29
 * @description: 初始化ChannelPipeline
 */
public class ChatServerInitializer extends ChannelInitializer<Channel> {



    private final ChannelGroup group;

    public ChatServerInitializer(ChannelGroup group) {
        this.group = group;
    }

    // 为了将ChannelHandler安装到ChannelPipeline中, 扩展了ChannelInitializer, 并实现initChannel方法
    /**
     * 对于initChannel()方法的调用, 通过安装所有必须的ChannelHandler来设置该新注册的Channel的ChannelPipeline
     * 1. 在WebSocket协议升级之前ChannelPipeline的状态:
     * HttpRequestDecoder -> HttpResponseEncoder ->HttpObjectAggregator ->HttpRequestHandler -> WebSocketServerProtocolHandler -> TextWebSocketFrameHandler
     *
     * 2. 当WebSocket协议升级完成后, WebSocketServerProtocolHandler 将会把 HttpRequestDecoder 替换为 WebSocketFrameDecoder. 把 HttpResponseEncoder 替换为
     *    WebSocketFrameEncoder。为了性能最大化，它将移除任何不再被 WebSocket 连接所需要的 ChannelHandler
     * WebSocketFrameDecoder -> WebSocketFrameEncoder -> WebSocketServerProtocolHandler -> TextWebSocketFrameHandler
     *
     * @param channel
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel channel) throws Exception {
        // 将所有需要的ChannelHandler添加到ChannelPipeline中
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new HttpServerCodec()); // 将字节解码为HttpRequest, HttpContent和LastHttpContent.并编码
        pipeline.addLast(new ChunkedWriteHandler()); // 写入一个文件的内容
        // 将一个HttpMessage和跟随它的多个HttpContent聚合为单个FullHttpRequest, 安装了这个之后, ChannelPipeline中的下一个ChannelHandler
        // 将只会受到完整的HTTP请求和响应
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        // 处理FullHttpRequest, 哪些不发送到/ws URI的请求
        pipeline.addLast(new HttpRequestHandler("/ws"));
        // 按照WebSocket规范的要求, 处理WebSocket升级握手, PingWebSocketFrame, PongWebSocketFrame和CloseWebSocketFrame
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        // 处理TextWebSocketFrame和握手完成事件
        pipeline.addLast(new TextWebSocketFrameHandler(group));
    }
}
