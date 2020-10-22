package com.yqq.nettydemo.server;

import com.yqq.nettydemo.server.initializer.ChatServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/22
 * @Time:10:01
 */
public class ChatServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup , workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChatServerInitializer());
        try{
            ChannelFuture channelFuture = serverBootstrap.bind(7798).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
