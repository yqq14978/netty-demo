package com.yqq.nettydemo.server.initializer;

import com.yqq.nettydemo.server.handler.FileServerHandler;
import com.yqq.nettydemo.server.handler.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
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
        //聚合器:不加聚合器的情况下，一次http请求默认会分位多次进行发送，第一次是httpRequest，后面多次都是httpContent
//        channelPipeline.addLast(new HttpObjectAggregator(1024*1024));
//        channelPipeline.addLast(new HttpContentCompressor());
        channelPipeline.addLast(new ChunkedWriteHandler());
        channelPipeline.addLast("handler" , new FileServerHandler());
    }
}
