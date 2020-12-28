package com.yqq.nettydemo.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.IOException;
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

    private static final String PATH = "files";
    private HttpRequest request;
    private HttpPostRequestDecoder postRequestDecoder;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端已连接：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        super.channelRead(ctx, msg);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handler已加入：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端事件触发：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开连接：" + ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        System.out.println("服务端读事件触发"+msg);
        //因为大文件需要进行分块传输所以第一次传输过来的会是request对象，后续的会是每一个数据块，需要使用HttpContent对象来处理
        if(msg instanceof HttpRequest){
            request = (HttpRequest) msg;
            //如果是post请求需要创建解码器进行处理
            if(HttpMethod.POST.equals(request.method())){
                postRequestDecoder = new HttpPostRequestDecoder(request);
            }
            ByteBuf buffer = Unpooled.copiedBuffer("已接收到文件上传请求" , CharsetUtil.UTF_8);
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1 , HttpResponseStatus.OK , buffer);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE , "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH , buffer.readableBytes());
//            response.headers().set(HttpHeaderNames.CONNECTION , HttpHeaderValues.CLOSE);

            ctx.write(response);
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else
            //对后续的数据块进行处理
            if (msg instanceof HttpContent && postRequestDecoder != null){
            HttpContent chunk = (HttpContent) msg;
            //对数据进行解码
            postRequestDecoder.offer(chunk);
            //判断是否是最后一部分数据块
            if(chunk instanceof LastHttpContent){
                request = null;
                //摧毁编码器，释放内存
                postRequestDecoder.destroy();
                postRequestDecoder = null;
            }else {
                //对数据进行读取
                readData();
            }
            ByteBuf buffer = Unpooled.copiedBuffer("已接收到文件上传请求" , CharsetUtil.UTF_8);
            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1 , HttpResponseStatus.OK , buffer);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE , "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH , buffer.readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION , HttpHeaderValues.KEEP_ALIVE);
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        }

    }

    private void readData() {
        while (postRequestDecoder.hasNext()){
            InterfaceHttpData httpData = postRequestDecoder.next();
            if(null != httpData){
                //写入目标文件夹
                writeData(httpData);
            }
        }
    }

    private void writeData(InterfaceHttpData data) {
        //判断是否是文件数据
        if(InterfaceHttpData.HttpDataType.FileUpload.equals(data.getHttpDataType())){
            FileUpload fileUpload = (FileUpload) data;
            if(fileUpload.isCompleted()){
                try {
                    fileUpload.renameTo(new File(PATH + "/" + fileUpload.getFilename()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
