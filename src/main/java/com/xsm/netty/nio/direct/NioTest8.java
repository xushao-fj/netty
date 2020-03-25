package com.xsm.netty.nio.direct;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xsm
 * @Date 2020/3/21 21:56
 * 直接缓冲
 */
public class NioTest8 {

    public static void main(String[] args) throws Exception{
        FileInputStream inputStream = new FileInputStream("src/input2.txt");

        FileOutputStream outputStream = new FileOutputStream("src/output2.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();
        // 参数为设置buffer的大小
        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        while (true){
            // 如果注释掉该行代码会发生什么情况?
            buffer.clear();
            int read = inputChannel.read(buffer);
            System.out.println("read: " + read);
            if (-1 == read){
                break;
            }
            buffer.flip();
            outputChannel.write(buffer);
        }
        inputChannel.close();
        outputChannel.close();
    }
}
