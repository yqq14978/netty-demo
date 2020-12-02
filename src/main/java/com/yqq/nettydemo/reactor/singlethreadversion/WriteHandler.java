package com.yqq.nettydemo.reactor.singlethreadversion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/1
 * @Time:11:38
 */
public class WriteHandler implements Runnable {

    private final Selector selector;
    private final SelectionKey selectionKey;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);

    public WriteHandler(Selector selector , SelectionKey selectionKey) {
        this.selector = selector;
        this.selectionKey = selectionKey;
        selectionKey.attach(this);
        //注册写事件
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    @Override
    public void run() {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        String info = "hello";
        buffer.put(info.getBytes());
        try {
            buffer.flip();
            socketChannel.write(buffer);
            System.out.println("执行写入操作完成");
            selectionKey.interestOps(SelectionKey.OP_READ);
            selectionKey.attach(new ReadHandler(selector , socketChannel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
