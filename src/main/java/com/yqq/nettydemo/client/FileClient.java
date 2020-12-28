package com.yqq.nettydemo.client;

import com.yqq.nettydemo.client.handler.FileClientHandler;
import com.yqq.nettydemo.client.initializer.FileClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.handler.codec.http2.Http2CodecUtil;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA
 *
 * @author:yeqq
 * @Date:2020/12/15
 * @Time:15:41
 */
public class FileClient {

    public static void sendmsg(Channel channel) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        for(;;){
            String filePath = bufferedReader.readLine();
            sendFileWithHttp(filePath , "localhost:8899" , channel);
        }
    }

    public static void sendFileWithHttp(String filePath , String uri , Channel channel) throws Exception {
        //D:\others\configserver\config\application.properties
        //F:\developTools\proj\netty-demo\clientFiles\16-9.mp4
//        channel.connect(new InetSocketAddress("localhost" , 8899)).sync();
        System.out.println("准备传输文件：" + channel.remoteAddress());
        File file = new File(filePath);
        if(!file.exists()){
            System.out.println("文件不存在");
            return;
        }
//        Map headers = new HashMap<>();
//        headers.put("file" , file);
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri);
        HttpHeaders headers = request.headers();
//        headers.set(HttpHeaderNames.HOST, "localhost");
//        headers.set(HttpHeaderNames.CONTENT_LENGTH , 0);
//        headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
//        headers.set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP + "," + HttpHeaderValues.DEFLATE);
//        headers.set(HttpHeaderNames.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
//        headers.set(HttpHeaderNames.ACCEPT_LANGUAGE, "fr");
//        headers.set(HttpHeaderNames.USER_AGENT, "Netty Simple Http Client side");
//        headers.set(HttpHeaderNames.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        HttpPostRequestEncoder postRequestEncoder = new HttpPostRequestEncoder(request , true);
        postRequestEncoder.addBodyFileUpload("file" , file.getName() , file , "application/octet-stream" , false);
        postRequestEncoder.finalizeRequest();
//        channel.write(request);
        channel.write(request);
        channel.write(postRequestEncoder);
        //HttpPostRequestEncoder会主动添加EMPTY_LAST_CONTENT，所以不需要额外发送
//        channel.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        channel.flush();
    }

    public static void main(String[] args) throws Exception {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                    .handler(new LoggingHandler()).handler(new FileClientInitalizer())
            .option(ChannelOption.SO_KEEPALIVE,true);

            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost" , 8899)).sync();
            sendmsg(channelFuture.channel());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
