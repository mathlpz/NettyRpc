package com.nettyrpc.io.netty.tcp;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.ws.Response;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {

    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

 

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

 

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Request request = (Request) msg;
        System.out.println("Server:"+ request.getId() + "," + request.getName() + "," +request.getReqeustMessag());

        Response response = new Response();
        response.setId(request.getId());
        response.setName("response "+ request.getId());
        response.setResponseMessage("响应内容：" +request.getReqeustMessag());

        byte[] unGizpData =GzipUtils.unGzip(request.getAttachment());
        char separator = File.separatorChar;
        FileOutputStream outputStream = newFileOutputStream(System.getProperty("user.dir") + separator +"recieve" + separator + "1.png");

        outputStream.write(unGizpData);
        outputStream.flush();
        outputStream.close();
        ctx.writeAndFlush(response);
    }

}