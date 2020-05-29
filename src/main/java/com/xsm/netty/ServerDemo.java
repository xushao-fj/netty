package com.xsm.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.zip.GZIPOutputStream;

/**
 * @author: xsm
 * @create: 2020-05-27
 * @description:
 * 重用eventLoop
 */
public class ServerDemo {

    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {

                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println("channelActive");
                        Bootstrap bootstrap1 = new Bootstrap();
                        bootstrap1.channel(NioSocketChannel.class)
                                .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
                                        System.out.println("Received data");
                                    }
                                });
                        bootstrap1.group(ctx.channel().eventLoop());
                        ChannelFuture channelFuture = bootstrap1.connect(new InetSocketAddress("www.baidu.com", 80));
                        channelFuture.addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                                if (channelFuture.isSuccess()) {
                                    System.out.println("connect success");
                                }
                                else {
                                    System.out.println("connect fail");
                                }
                            }
                        });
                    }

                    @Override
                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {

                    }

                });
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

            }
        });
    }
}
