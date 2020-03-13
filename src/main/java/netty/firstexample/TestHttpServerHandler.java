package netty.firstexample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author xsm
 * @Date 2020/3/13 22:28
 * SimpleChannelInboundHandler 进来请求的处理
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    /**
     * 读取客户端的请求, 并且向客户端返回响应
     * @param channelHandlerContext
     * @param httpObject
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {

        // 查看请求实例
        System.out.println(httpObject.getClass());

        // 获取远程地址
        System.out.println(channelHandlerContext.channel().remoteAddress());

        if (httpObject instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) httpObject;
            System.out.println("请求方法名:" + httpRequest.method().name());
            URI uri = new URI(httpRequest.getUri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求favicon.ico");
                return;
            }
            ByteBuf content = Unpooled.copiedBuffer("Hello world", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            // http 响应头
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            // 响应内容
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
            channelHandlerContext.writeAndFlush(response);
            // 关闭链接, 但是需要判断请求是http1.0(短连接), 还是http1.1(长连接), 如果是http1.1才需要关闭链接
            // 关闭链接, 但是需要判断请求是http1.0(短连接), 还是http1.1(长连接), 如果是http1.1才需要关闭链接
            channelHandlerContext.channel().close();
        }
    }

    // 以上为第一个hello world 程序


    // 以下复写方法
    // 以下方法可以看出netty接收请求的执行流程

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active");
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler added");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel unregistered");
        super.channelUnregistered(ctx);
    }
}
