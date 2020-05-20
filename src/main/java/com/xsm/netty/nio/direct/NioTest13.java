package com.xsm.netty.nio.direct;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * @author xsm
 * @Date 2020/4/11 12:17
 * java 编解码
 */
public class NioTest13 {

    public static void main(String[] args) throws Exception{
         String inputFile = "src/NioTest13_In.txt";
         String outputFile = "src/NioTest13_Out.txt";

        RandomAccessFile inputRandomAccessFile = new RandomAccessFile(inputFile, "r");
        RandomAccessFile outputRandomAccessFile = new RandomAccessFile(outputFile, "rw");

        long inputLength = new File(inputFile).length();

        FileChannel inputFileChannel = inputRandomAccessFile.getChannel();
        FileChannel outputFileChannel = outputRandomAccessFile.getChannel();

        MappedByteBuffer inputData = inputFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, inputLength);

        System.out.println("====================");
        Charset.availableCharsets().forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });

        System.out.println("====================");

        // 为什么iso-8859-1在这个程序中不会有乱码现象? 为什么在其他地方写代码会有乱码的情况?
        Charset charset = Charset.forName("iso-8859-1"); // utf-8, iso-8859-1
        // 解码
        CharsetDecoder decoder = charset.newDecoder();
        // 编码
        CharsetEncoder encoder = charset.newEncoder();


        CharBuffer charBuffer = decoder.decode(inputData);

        ByteBuffer outputData = encoder.encode(charBuffer);
        // 编解码不一致时就会产生乱码
        // ByteBuffer outputData = Charset.forName("utf-8").encode(charBuffer);

        outputFileChannel.write(outputData);

        inputRandomAccessFile.close();
        outputRandomAccessFile.close();
    }

}
