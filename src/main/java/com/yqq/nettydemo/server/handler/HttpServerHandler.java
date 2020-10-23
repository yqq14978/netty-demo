package com.yqq.nettydemo.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;



/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/10/20
 * @Time:14:20
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        System.out.println("channelRead0接收消息");
        if(httpObject instanceof HttpRequest){
            ByteBuf buffer = Unpooled.copiedBuffer("hello world!" , CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1 , HttpResponseStatus.OK , buffer);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE , "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH , buffer.readableBytes());
            channelHandlerContext.writeAndFlush(response);
        }
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("channelRead0接收消息");
//        if(msg instanceof HttpRequest){
//            ByteBuf buffer = Unpooled.copiedBuffer("hello world!" , CharsetUtil.UTF_8);
//            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1 , HttpResponseStatus.OK , buffer);
//            response.headers().set(HttpHeaderNames.CONTENT_TYPE , "text/plain");
//            response.headers().set(HttpHeaderNames.CONTENT_LENGTH , buffer.readableBytes());
//            ctx.writeAndFlush(response);
//        }
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
    }
}
