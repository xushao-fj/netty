package com.xsm.netty.learn.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xsm
 * @Date 2020/5/31 15:57
 */
public class Reactor implements Runnable{

    final Selector selector;

    final ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 注册接收事件
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> sks = selector.selectedKeys();
                Iterator<SelectionKey> it = sks.iterator();
                while (it.hasNext()) {
                    // 分发处理
                    dispatcher(it.next());
                }
                // 处理完后要将事件移除掉, 避免重复处理
                sks.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatcher(SelectionKey sk) {
        Runnable r = (Runnable) sk.attachment();
        if (r != null) {
            r.run();
        }
    }

    class Acceptor implements Runnable {

        @Override
        public void run() {
            try {
                SocketChannel c = serverSocketChannel.accept();
                if (c != null) {
                    new Handler(selector, c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    final class Handler implements Runnable {

        final SocketChannel socket;

        final SelectionKey sk;

        static final int MAX_IN = 1024;
        static final int MAX_OUT = 1024;
        ByteBuffer input = ByteBuffer.allocate(MAX_IN);
        ByteBuffer output = ByteBuffer.allocate(MAX_OUT);
        static final int READING = 0;
        static final int SENDING = 1;
        int state = READING;

        public Handler(Selector sel, SocketChannel c) throws IOException{
            socket = c;
            c.configureBlocking(false);
            // 也可以注册SelectionKey.OP_READ, 这里先不关心任何事件, 后面注册读事件
            sk = socket.register(sel, 0);
            sk.attach(this);
            sk.interestOps(SelectionKey.OP_READ);
            sel.wakeup();
        }

        boolean inputIsComplete(){
            // 加入实现
            return true;
        }

        boolean outputIsComplete(){
            // 加入实现
            return true;
        }

        void process(){
            // 加入实现
        }

        @Override
        public void run() {
            try {
                if (state == READING) {
                    read();
                } else if (state == SENDING){
                    write();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void read() throws IOException{
            socket.read(input);
            process();
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        }

        private void write() throws IOException{
            socket.write(output);
            if (outputIsComplete()) {
                sk.cancel();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(8899);
        reactor.run();
    }
}
