package com.yqq.nettydemo.client.initializer;

import com.yqq.nettydemo.client.handler.FileClientHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
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
        //解码器和编码器
        pipeline.addLast(new HttpClientCodec());
        //聚合器:不加聚合器的情况下，一次http请求默认会分位多次进行发送，第一次是httpRequest，后面多次都是httpContent
//        pipeline.addLast(new HttpObjectAggregator(1024*1024));
        //解压
//        pipeline.addLast(new HttpContentCompressor());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast("handler" , new FileClientHandler());
    }
}
