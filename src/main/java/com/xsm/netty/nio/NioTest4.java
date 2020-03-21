package com.xsm.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xsm
 * @Date 2020/3/21 21:01
 */
public class NioTest4 {


    public static void main(String[] args)  throws Exception{
        FileInputStream inputStream = new FileInputStream("src/input.txt");

        FileOutputStream outputStream = new FileOutputStream("src/output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();
        // 参数为设置buffer的大小
        ByteBuffer buffer = ByteBuffer.allocate(512);
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
