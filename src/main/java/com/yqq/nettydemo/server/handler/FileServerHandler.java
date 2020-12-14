package com.yqq.nettydemo.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpObject;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/14
 * @Time:16:06
 */
public class FileServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

//        FileChannel fileChannel =
    }
}
