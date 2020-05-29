package com.xsm.netty.learn.websocket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author: xsm
 * @create: 2020-05-29
 * @description: 扩展SimpleChannelInboundHandler 以处理 FullHttpRequest消息
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;

    private static final File INDEX;

    public HttpRequestHandler (String wsUri){
        this.wsUri = wsUri;
    }

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "index.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate index.html", e);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.uri())) {
            // 如果请求了WebSocket协议升级, 则增加引用计数(调用retain()方法), 并将它传递给下一个ChannelInboundHandler
            ctx.fireChannelRead(request.retain());
        }
        else {
            if (HttpHeaders.is100ContinueExpected(request)) {
                // 处理请求100 Continue请求以符合HTTP1.1规范
                send100Continue(ctx);
            }
            // 读取index.html
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");
            DefaultHttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
            boolean keepAlive = HttpHeaders.isKeepAlive(request);
            if (keepAlive) {
                // 如果请求了keep-alive, 则添加所需要的HTTP头信息
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            // 将HttpResponse写到客户端
            ctx.write(response);
            if (ctx.pipeline().get(SslHandler.class) == null) {
                // index.html写到客户端
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            }
            else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }
            // 写LastHttpContent并冲刷至客户端
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive) {
                // 如果没有请求keep-alive, 则在写操作完成后关闭Channel
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
