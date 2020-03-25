package com.xsm.netty.nio.direct;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author xsm
 * @Date 2020/3/22 20:10
 */
public class NioTest9 {

    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("src/NioTest9.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        // 第一个参数: 映射模式, 第二个参数: 映射多少
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte)'a');
        mappedByteBuffer.put(3, (byte)'b');
        randomAccessFile.close();
    }
}
