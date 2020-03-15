package com.xsm.netty.fifthexample;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author xsm
 * @Date 2020/3/15 11:24
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // websocket是基于http,所以也需要http编解码
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        // netty对于请求是分块分段的, 比如请求是1000字节, 可能会分块处理
        // HttpObjectAggregator对http消息进行聚合, 用于处理请求或者响应
        pipeline.addLast(new HttpObjectAggregator(8192));
        // WebSocketServerProtocolHandler处理websocket, websocket服务端协议处理器
        // 负责websocket的握手, ping, pong, 文本, 二进制数据处理
        // websocketPath, websocket的uri地址
        // ws://localhost:8080/ws, "/ws"指定的是最后的
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

        pipeline.addLast(new TextWebSocketFrameHandler());
    }
}
