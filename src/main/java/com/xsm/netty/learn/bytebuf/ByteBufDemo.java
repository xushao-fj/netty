package com.xsm.netty.learn.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author xsm
 * @Date 2020/5/22 22:38
 */
public class ByteBufDemo {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer();
        System.out.println("readerIndex: " + buffer.readerIndex());
        System.out.println("writerIndex: " + buffer.writerIndex());
        System.out.println("capacity: " + buffer.capacity());
        buffer.writeBytes("hollow,world".getBytes());
        for (int i = 0; i < buffer.capacity(); i ++) {
            byte b = buffer.getByte(i);
            System.out.println((char)b);
        }
        System.out.println("==================");
        ByteBuf readBytes = buffer.readBytes(6);
        for (int i = 0; i < readBytes.capacity(); i ++) {
            byte b = buffer.getByte(i);
            System.out.println((char)b);
        }
        System.out.println("=================");
        while (buffer.isReadable()) {
            System.out.println(buffer.readByte());
        }
        System.out.println("readerIndex: " + buffer.readerIndex());
        System.out.println("writerIndex: " + buffer.writerIndex());

    }
}
