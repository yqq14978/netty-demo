package com.yqq.nettydemo.reactor.singlethreadversion;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/1
 * @Time:10:44
 */
public class Reactor implements Runnable{

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    public Reactor(int port) throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);

        this.selector = Selector.open();
        //将每一个接收的请求交给Acceptor去处理
        serverSocketChannel.register(selector , SelectionKey.OP_ACCEPT , new Acceptor(selector , serverSocketChannel));
    }

    @Override
    public void run() {
        for (;;){
            try {
                //阻塞
                selector.select();
                //事件触发后进行分发操作
                dispatch();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatch(){
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
        while (keyIterator.hasNext()){
            SelectionKey selectionKey = keyIterator.next();
            //根据不同的事件进行分发，交由不同的handler进行处理
            if(selectionKey.isReadable()){
                ReadHandler readHandler = (ReadHandler) selectionKey.attachment();
                readHandler.run();
            }else if(selectionKey.isWritable()){
                WriteHandler writeHandler = (WriteHandler) selectionKey.attachment();
                writeHandler.run();
            }else {
                Acceptor acceptor = (Acceptor) selectionKey.attachment();
                acceptor.run();
            }
        }
        selectionKeys.clear();
    }
}
