package com.xsm.netty.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xsm
 * @Date 2020/3/21 10:56
 * 读入文件数据
 *
 */
public class NioTest2 {
    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("src/NioTest2.txt");
        // 获取通道
        FileChannel fileChannel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        // 写入数据
        fileChannel.read(byteBuffer);
        // 读写转换
        byteBuffer.flip();
        while (byteBuffer.remaining() > 0){
            byte b = byteBuffer.get();
            System.out.println("Character: " + (char)b);
        }
        fileInputStream.close();
    }
}
