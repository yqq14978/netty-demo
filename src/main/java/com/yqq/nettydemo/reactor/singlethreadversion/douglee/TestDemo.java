package com.yqq.nettydemo.reactor.singlethreadversion.douglee;

import java.io.IOException;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/1
 * @Time:15:41
 */
public class TestDemo {

    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(9090);
        Thread thread = new Thread(reactor);
        thread.start();
    }
}
