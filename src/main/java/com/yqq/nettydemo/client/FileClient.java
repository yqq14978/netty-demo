package com.yqq.nettydemo.client;

import com.yqq.nettydemo.client.initializer.FileClientInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
            bootstrap.group(workerGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler()).handler(new FileClientInitalizer());

            ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost" , 8899)).sync();
            future.channel().closeFuture().sync();
            for(;;){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                String filePath = bufferedReader.readLine();
                sendFileWithHttp(filePath);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static void sendFileWithHttp(String filePath) {
        File file = new File(filePath);
        if(!file.exists()){
            System.out.println("文件不存在");
            return;
        }
        Map headers = new HashMap<>();
        headers.put("file" , file);
    }

}
