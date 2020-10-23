package com.yqq.nettydemo.client;

import com.yqq.nettydemo.client.initializer.HeartBeatClientInitializer;
import com.yqq.nettydemo.server.initializer.HeartBeatServerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/23
 * @Time:11:22
 */
public class HeartBeatClient {

    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new HeartBeatClientInitializer());

            Channel channel = bootstrap.connect("localhost" , 5566).sync().channel();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            for (; ;){
                channel.writeAndFlush(bufferedReader.readLine() + "\r\n");
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
