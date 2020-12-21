package com.yqq.nettydemo.client;

import com.yqq.nettydemo.client.initializer.FileClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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

    public static void main(String[] args) {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                    .handler(new LoggingHandler()).handler(new FileClientInitalizer());

            ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost" , 8899)).sync();
//            future.channel().closeFuture().sync();
            for(;;){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String filePath = bufferedReader.readLine();
                sendFileWithHttp(filePath , "localhost:8899" , future.channel());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpPostRequestEncoder.ErrorDataEncoderException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static void sendFileWithHttp(String filePath , String uri , Channel channel) throws HttpPostRequestEncoder.ErrorDataEncoderException {
        //D:\others\configserver\config\application.properties
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
        channel.write(request);
        channel.write(postRequestEncoder);
        channel.flush();
    }

}
