package com.yqq.nettydemo.reactor.singlethreadversion.douglee;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/1
 * @Time:16:09
 */
public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;
    Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT); //注册accept事件
        sk.attach(new Acceptor()); //调用Acceptor()为回调方法
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {//循环
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext()){
                    dispatch((SelectionKey)(it.next())); //dispatch分发事件
                }
                selected.clear();
            }
        } catch (IOException ex) { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable)(k.attachment()); //调用SelectionKey绑定的调用对象
        if (r != null){
            r.run();
        }
    }

    // Acceptor 连接处理类
    class Acceptor implements Runnable { // inner
        @Override
        public void run() {
            try {
                SocketChannel c = serverSocket.accept();
                if (c != null){
                    new Handler(selector, c);
                }
            }
            catch(IOException ex) { /* ... */ }
        }
    }
}
