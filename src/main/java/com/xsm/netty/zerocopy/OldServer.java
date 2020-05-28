package com.xsm.netty.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xsm
 * @Date 2020/5/28 23:06
 * 传统实现方式
 */
public class OldServer {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(8899);

        while (true) {
            // 监听并接收链接, 阻塞
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            try {
              byte[] byteArray = new byte[4096];
              while (true) {
                  int readCount = dataInputStream.read(byteArray, 0, byteArray.length);
                  if (-1 == readCount) {
                      break;
                  }
              }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

}
