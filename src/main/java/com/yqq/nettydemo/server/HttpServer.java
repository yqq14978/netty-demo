package com.yqq.nettydemo.server;

import com.yqq.nettydemo.server.initializer.HttpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/20
 * @Time:14:07
 */
public class HttpServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGruop = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup , workerGruop).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer());

            ChannelFuture future = serverBootstrap.bind(7788).sync();
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGruop.shutdownGracefully();
        }
    }
}
