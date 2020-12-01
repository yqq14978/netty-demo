package com.yqq.nettydemo.reactor.singlethreadversion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/1
 * @Time:11:38
 */
public class ReadHandler implements Runnable {

    private final Selector selector;
    private final SocketChannel socketChannel;
    private final SelectionKey selectionKey;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public ReadHandler(Selector selector , SocketChannel socketChannel) throws IOException {
        this.selector = selector;
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        //监听写事件
        selectionKey = socketChannel.register(selector , SelectionKey.OP_READ , this);
        //事件完成后唤醒阻塞的selector.select()方法
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            //进行数据的读取操作
            socketChannel.read(buffer);
            //进行业务操作
            process();
            //业务操作完成后进行写事件的监听
            new WriteHandler(selectionKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process() throws IOException {
        System.out.println(socketChannel.getRemoteAddress());
    }
}
