package com.yqq.nettydemo.client;

import com.yqq.nettydemo.client.handler.FileClientHandler;
import com.yqq.nettydemo.client.initializer.FileClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
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
    ChannelFuture channelFuture = null;

    public void init(){
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                    .handler(new LoggingHandler()).handler(new FileClientInitalizer());

            channelFuture = bootstrap.connect(new InetSocketAddress("localhost" , 8899));
            channelFuture.sync().isSuccess();
//            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void sendmsg(Channel channel) throws HttpPostRequestEncoder.ErrorDataEncoderException, InterruptedException, IOException {
        //            future.channel().closeFuture().sync();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        for(;;){
            String filePath = bufferedReader.readLine();
            sendFileWithHttp(filePath , "localhost:8899" , channel);
//        }
    }

    public void sendFileWithHttp(String filePath , String uri , Channel channel) throws HttpPostRequestEncoder.ErrorDataEncoderException, InterruptedException {
        //D:\others\configserver\config\application.properties
        //F:\developTools\proj\netty-demo\clientFiles\16-9.mp4
        System.out.println("准备传输文件：" + channel.remoteAddress());
        File file = new File(filePath);
        if(!file.exists()){
            System.out.println("文件不存在");
            return;
        }
//        Map headers = new HashMap<>();
//        headers.put("file" , file);
        String[] filePaths = filePath.split("/");
        String fileName = filePaths[filePaths.length - 1];
        HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, uri);
        HttpPostRequestEncoder postRequestEncoder = new HttpPostRequestEncoder(request , true);
        postRequestEncoder.addBodyFileUpload("file" , fileName , file , "application/octet-stream" , false);
        postRequestEncoder.finalizeRequest();
        ChannelFuture future = channel.writeAndFlush(request);
        System.out.println(future.await().isDone());
//        channel.write(postRequestEncoder);
//        channel.flush();
    }

    public static void main(String[] args) throws InterruptedException {
        FileClient fileClient = new FileClient();
        fileClient.init();
//        FileClient.init();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    fileClient.sendmsg(fileClient.channelFuture.channel());
//                } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
//                    e.printStackTrace();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }
}
