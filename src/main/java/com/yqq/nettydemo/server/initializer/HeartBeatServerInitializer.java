package com.yqq.nettydemo.server.initializer;

import com.yqq.nettydemo.server.handler.HeartBeatServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/23
 * @Time:10:58
 */
public class HeartBeatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(5 , 7 , 10));
//        pipeline.addLast(new DelimiterBasedFrameDecoder(4096 , Delimiters.lineDelimiter()));
        pipeline.addLast(new HeartBeatServerHandler());
    }
}
