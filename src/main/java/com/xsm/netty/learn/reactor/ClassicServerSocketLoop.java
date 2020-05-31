package com.xsm.netty.learn.reactor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xsm
 * @Date 2020/5/31 15:44
 */
public class ClassicServerSocketLoop {

    private static final int PORT = 8899;

    private static final int MAX_INPUT = 1024;

    class Server implements Runnable {

        @Override
        public void run() {
            try (ServerSocket ss = new ServerSocket(PORT)){
                while (!Thread.interrupted()) {
                    new Thread(new Handler(ss.accept())).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Handler implements Runnable{

        final Socket soket;

        public Handler(Socket soket) {
            this.soket = soket;
        }

        @Override
        public void run() {
            try {
                byte[] input = new byte[MAX_INPUT];
                soket.getInputStream().read(input);
                byte[] output = process(input);
                soket.getOutputStream().write(output);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private byte[] process(byte[] input) {
            // 业务操作
            return new byte[0];
        }
    }
}
