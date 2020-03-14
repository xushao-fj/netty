package netty.thirdexample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author xsm
 * @Date 2020/3/14 21:20
 * 服务端
 * 启动, 等待建立连接, 和客户端通信
 * a,b,c三个客户端, 服务端有新链接, 打印链接, 客户端下线, 打印下线链接, 通知谁上线和下线, 并推送相关发送的消息
 */
public class MyChatServer {
    public static void main(String[] args) {
        // 定义两个事件循环组
        // 获取连接, 接收请求
        EventLoopGroup bossGroup= new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new MyChatInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
