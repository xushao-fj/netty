package com.xsm.netty.learn.firstdemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author xsm
 * @Date 2020/5/19 22:39
 * 1. 绑定到服务器将在其上监听并接收传入链接的请求
 * 2. 配置Channel, 以将有关的入站消息通知给EchoServerHandler
 */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception{

        int port = 9999;
        new EchoServer(port).start();
    }

    private void start() throws Exception{
        final EchoServerHandler serverHandler = new EchoServerHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();

            b.group(group)
             .channel(NioServerSocketChannel.class) // 指定所使用的NIO传输Channle
             .localAddress(new InetSocketAddress(port)) // 使用指定的端口设置套接字地址
             .childHandler(new ChannelInitializer<SocketChannel>() { // 添加一个EchoServerHandler到子Channle的ChannelPipeline
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // EchoServerHandlerbei 被标注为@Shareable, 所以我们可以总是使用同样的实例
                    ch.pipeline().addLast(serverHandler);
                    // 异步绑定服务器, 调用sync()方法阻塞等待直到绑定完成
                    ChannelFuture f = b.bind().sync();
                    // 获取Channel的CloseFuture, 并且阻塞当前线程直到它完成
                    f.channel().closeFuture().sync();
                }
            });
        } finally {
            // netty优雅关闭
            group.shutdownGracefully().sync();
        }

    }

}
