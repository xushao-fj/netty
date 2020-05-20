package com.xsm.netty.nio.direct;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author xsm
 * @Date 2020/4/11 11:15
 */
public class NioServer {

    private static final Map<String, SocketChannel> CLIENT_MAP = new HashMap<>();

    public static void main(String[] args) throws Exception{
        // 服务器端
        // 1. ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 2. 设置为非阻塞的
        serverSocketChannel.configureBlocking(false);
        // 3. 获取socket对象
        ServerSocket serverSocket = serverSocketChannel.socket();
        // 4. 绑定端口
        serverSocket.bind(new InetSocketAddress(8899));

        Selector selector = Selector.open();
        // 关注连接事件, 将channel对象注册到selector上
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("selectionKey: " + key);

        while (true) {
            try {
                // 返回所关注的对象的数量
                int select = selector.select();
                // 返回所有注册的SelectionKey集合
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                selectionKeys.forEach(selectionKey -> {
                    final SocketChannel client;

                    try {
                        if (selectionKey.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                            client = server.accept();
                            client.configureBlocking(false);
                            // 客户端已和服务端建立连接
                            client.register(selector, SelectionKey.OP_READ);

                            // 将客户端连接放入map内存中
                            String mapKey = UUID.randomUUID().toString();
                            CLIENT_MAP.put(mapKey, client);
                        } else if (selectionKey.isReadable()) {
                            client = (SocketChannel) selectionKey.channel();
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            int count = client.read(readBuffer);
                            if (count > 0) {
                                readBuffer.flip();
                                Charset charset = Charset.forName("utf-8");
                                String receivedmessage = String.valueOf(charset.decode(readBuffer).array());
                                System.out.println(client + ": " + receivedmessage);
                                String senderKey = null;
                                for (Map.Entry<String, SocketChannel> entry : CLIENT_MAP.entrySet()) {
                                    if (client == entry.getValue()) {
                                        senderKey = entry.getKey();
                                        break;
                                    }
                                }
                                for (Map.Entry<String, SocketChannel> entry : CLIENT_MAP.entrySet()) {
                                    SocketChannel value = entry.getValue();
                                    ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                                    writeBuffer.put((senderKey + ": " + receivedmessage).getBytes());
                                    writeBuffer.flip();
                                    value.write(writeBuffer);
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                selectionKeys.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
