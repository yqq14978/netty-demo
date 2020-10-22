package com.yqq.nettydemo.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/22
 * @Time:10:57
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
        channelHandlerContext.writeAndFlush(System.in);
    }
}
