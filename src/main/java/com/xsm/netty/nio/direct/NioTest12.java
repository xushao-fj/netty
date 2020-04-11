package com.xsm.netty.nio.direct;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xsm
 * @Date 2020/3/25 22:49
 * 通过一个线程处理所有客户端的请求
 */
public class NioTest12 {

    public static void main(String[] args) throws Exception {
        // 服务端监听5个端口号
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        // 创建一个selector
        Selector selector = Selector.open();
        //System.out.println(SelectorProvider.provider().getClass());

        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 需要绑定到特定的地址端口上
            // true 阻塞, false 非阻塞
            serverSocketChannel.configureBlocking(false);
            // 与这个channel所关联的socket
            ServerSocket socket = serverSocketChannel.socket();
            InetSocketAddress address = new InetSocketAddress(ports[i]);
            socket.bind(address);
            // 注册
            // 将当前的selector注册到channel上, 并设置为感兴趣
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("监听端口: " + ports[i]);
        }

        // 死循环
        while (true) {
            int numbers = selector.select();
            System.out.println("numbers: " + numbers);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys: " + selectionKeys);
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 必须移除
                    iterator.remove();
                    System.out.println("获得客户端连接: " + socketChannel);
                } else if (selectionKey.isReadable()) {
                    // 是否可读
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    int bytesRead = 0;
                    while (true) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        byteBuffer.clear();
                        int read = socketChannel.read(byteBuffer);
                        if (read <= 0) {
                            break;
                        }
                        byteBuffer.flip();
                        socketChannel.write(byteBuffer);
                        bytesRead += read;
                    }
                    System.out.println("读取: " + bytesRead + ", 来自于: " + socketChannel);
                    // 当前事件消费掉, 一定要remove
                    iterator.remove();
                }
            }
        }

    }
}
