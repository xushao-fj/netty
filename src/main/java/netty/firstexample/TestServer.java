package netty.firstexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author xsm
 * @Date 2020/3/13 22:19
 */
public class TestServer {
    public static void main(String[] args) {
        // 定义时间循环组
        // 获取连接, 接收请求
        EventLoopGroup bossGroup= new NioEventLoopGroup();
        //
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 启动服务端的类
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class).childHandler(new TestServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // netty优雅关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }
}
