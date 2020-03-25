package com.xsm.netty.nio.direct;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author xsm
 * @Date 2020/3/25 22:04
 * 文件锁
 */
public class NioTest10 {

    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("src/NioTest10.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        // 位置, 长度, 是否共享
        FileLock fileLock = fileChannel.lock(3, 6, true);
        System.out.println("valid: " + fileLock.isValid());
        System.out.println("lock type: " + fileLock.isShared());

        fileLock.release();
        randomAccessFile.close();
    }

}
