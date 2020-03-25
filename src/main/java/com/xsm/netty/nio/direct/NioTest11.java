package com.xsm.netty.nio.direct;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author xsm
 * @Date 2020/3/25 22:07
 * 关于Buffer 的Scattering于Gathering
 */
public class NioTest11 {

    public static void main(String[] args) throws Exception{
        // Scattering 分散
        // Gathering 汇集
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        // 构造三个buffer
        int messageLength = 2 + 3 + 4;
        ByteBuffer[] buffers = new ByteBuffer[3];
        buffers[0] = ByteBuffer.allocate(2);
        buffers[1] = ByteBuffer.allocate(3);
        buffers[2] = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();

        while (true) {
            int bytesRead = 0;

            while (bytesRead < messageLength) {
                // 如果读到的字节小于总得字节数
                long r = socketChannel.read(buffers);
                bytesRead += r;

                System.out.println("bytesRead: " + bytesRead);
                Arrays.asList(buffers).stream()
                        .map(buffer -> "position: " + buffer.position() + ", limit: " + buffer.limit())
                        .forEach(System.out::println);

            }
            Arrays.asList(buffers).forEach(Buffer::flip);
            long bytesWritten = 0;
            while (bytesWritten < messageLength) {
                long r = socketChannel.write(buffers);
                bytesWritten += r;
            }
            Arrays.asList(buffers).forEach(Buffer::flip);
            System.out.println("bytesRead: " + bytesRead + ", bytesWritten: "
                    + bytesWritten + ", messageLength: " + messageLength);
        }

        // 如何测试, cmd, 然后输入 telnet localhost 8899
        // 然后输入内容, 回车, 查看控制台输出
    }
}
