package com.xsm.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xsm
 * @Date 2020/3/21 11:02
 * 写入文件数据
 */
public class NioTest3 {

    public static void main(String[] args) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream("src/NioTest3.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byte[] messages = "hello world welcome, nihao".getBytes();
        for (int i = 0; i < messages.length; i++) {
            byteBuffer.put(messages[i]);
        }
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileChannel.close();
    }
}
