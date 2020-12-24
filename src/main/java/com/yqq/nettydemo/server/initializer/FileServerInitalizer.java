package com.yqq.nettydemo.server.initializer;

import com.yqq.nettydemo.server.handler.FileServerHandler;
import com.yqq.nettydemo.server.handler.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

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

        channelPipeline.addLast(new HttpServerCodec());
//        channelPipeline.addLast(new HttpContentCompressor());
        channelPipeline.addLast(new ChunkedWriteHandler());
        channelPipeline.addLast("handler" , new FileServerHandler());
    }
}
