package com.xsm.netty.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @author xsm
 * @Date 2020/5/28 23:19
 * 对应 doc/picture -> 传统文件拷贝.png
 */
public class OldIOClient {

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost", 8899);
        String fileName = "E:/upload.rar"; // 指定文件
        FileInputStream inputStream = new FileInputStream(fileName);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] buffer = new byte[4096];
        long readCount;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while (((readCount = inputStream.read(buffer)) >= 0)) {
            total += readCount;
            dataOutputStream.write(buffer);
        }
        System.out.println("发送总字节数: " + total + ", 耗时: " + (System.currentTimeMillis() - startTime));
        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
