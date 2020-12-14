package com.yqq.nettydemo.server.initializer;

import com.yqq.nettydemo.server.handler.FileServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/14
 * @Time:15:49
 */
public class FileServerInitalizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();

        channelPipeline.addLast(new FileServerHandler());
    }
}
