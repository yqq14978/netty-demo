package com.yqq.nettydemo.client.initializer;

import com.yqq.nettydemo.client.handler.FileClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/15
 * @Time:15:55
 */
public class FileClientInitalizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpClientCodec());
        pipeline.addLast(new HttpContentCompressor());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new FileClientHandler());
    }
}
