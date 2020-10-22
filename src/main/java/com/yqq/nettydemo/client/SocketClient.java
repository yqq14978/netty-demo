package com.yqq.nettydemo.client;

import com.yqq.nettydemo.client.initializer.SocketClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/21
 * @Time:9:11
 */
public class SocketClient {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new SocketClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect("localhost" , 7789).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

}
