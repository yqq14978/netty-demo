package com.yqq.nettydemo.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/22
 * @Time:10:15
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static List<Channel> channels = new ArrayList<Channel>();
    private static ChannelGroup channelGroup= new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "：已连接");
        for (Channel channel : channels){
            channel.writeAndFlush("【客户端】" + ctx.channel().remoteAddress() + "：已上线\n");
        }
        channels.add(ctx.channel());
        System.out.println("当前在线人数——" + channels.size());
//        channelGroup.writeAndFlush("【服务器】" + ctx.channel().remoteAddress() + "已上线\n");
//        channelGroup.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        for (Channel channel : channels){
            String sender = "";
            if(channelHandlerContext.channel().remoteAddress().equals(channel.remoteAddress())){
                sender = "自己";
            }else {
                sender = channelHandlerContext.channel().remoteAddress().toString();
            }
            channel.writeAndFlush("【客户端】" + sender + "：" + s + "\n");
            System.out.println("消息接收方——" + channel.remoteAddress());
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        int index = -1;
        for (int i = 0; i < channels.size() ; i++){
            if(channels.get(i).remoteAddress().equals(ctx.channel().remoteAddress())){
                index = i;
            }else {
                channels.get(i).writeAndFlush("【客户端】" + ctx.channel().remoteAddress() + "：已下线\n");
            }
        }
        channels.remove(index);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
