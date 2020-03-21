package com.xsm.netty.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author xsm
 * @Date 2020/3/21 10:04
 * Buffer 中的capacity, position, limit
 */
public class NioConstructorTest {

    public static void main(String[] args) {
        // java nio 都在nio包下
        IntBuffer buffer = IntBuffer.allocate(10);
        System.out.println("capacity: " + buffer.capacity());
        for (int i = 0; i < 5; i++) {
            int randomNumber = new SecureRandom().nextInt(20);
            // 数据写入buffer
            buffer.put(randomNumber);
        }
        System.out.println("before flip limit: " + buffer.limit());
        // 读写切换
        buffer.flip();
        System.out.println("after flip limit: " + buffer.limit());
        System.out.println("enter while loop");
        while (buffer.hasRemaining()){
            System.out.println("position: " + buffer.position());
            System.out.println("limit: " + buffer.limit());
            System.out.println("capacity: " + buffer.capacity());
            // 数据写出
            System.out.println(buffer.get());
        }
    }

}
