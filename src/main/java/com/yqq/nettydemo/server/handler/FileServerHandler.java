package com.yqq.nettydemo.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

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
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //因为大文件需要进行分块传输所以第一次传输过来的会是request对象，后续的会是每一个数据块，需要使用HttpContent对象来处理
        if(msg instanceof HttpRequest){
            request = (HttpRequest) msg;
            //如果是post请求需要创建解码器进行处理
            if(HttpMethod.POST.equals(request.method())){
                postRequestDecoder = new HttpPostRequestDecoder(request);
            }
        } else
            //对后续的数据块进行处理
            if (msg instanceof HttpContent && postRequestDecoder != null){
            HttpContent chunk = (HttpContent) msg;
            //对数据进行解码
            postRequestDecoder.offer(chunk);
            //对数据进行读取
            readData();
            //判断是否是最后一部分数据块
            if(chunk instanceof LastHttpContent){
                request = null;
                //摧毁编码器，释放内存
                postRequestDecoder.destroy();
                postRequestDecoder = null;
            }
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
                    fileUpload.renameTo(new File(PATH + fileUpload.getFilename()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
