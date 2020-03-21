package com.xsm.netty.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author xsm
 * @Date 2020/3/21 10:04
 * nio 操作
 */
public class NioTest1 {

    public static void main(String[] args) {
        // java nio 都在nio包下
        IntBuffer buffer = IntBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            // 数据写入buffer
            buffer.put(randomNumber);

        }
        // 读写切换
        buffer.flip();
        while (buffer.hasRemaining()){
            // 数据写出
            System.out.println(buffer.get());
        }
    }

}
