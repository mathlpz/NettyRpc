package com.nettyrpc.io.netty.tcp;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Client {

    public static void main(String[] args) {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });

            ChannelFuture future =bootstrap.connect(new InetSocketAddress("127.0.01", 8765)).sync();

            for(int i=1; i<=5; i++) {
                Request request = new Request();
                request.setId(i);
                request.setName("pro"+ i);
                request.setReqeustMessag("数据信息" + i);
                //传输图片
                char separator = File.separatorChar;
                File file = new File(System.getProperty("user.dir") + separator + "source"+ separator + "2.jpg");
                FileInputStream inputStream = new FileInputStream(file);
                byte[] data = new byte[inputStream.available()];
                inputStream.read(data);
                inputStream.close();
                byte[] gzipData =GzipUtils.gzip(data);
                request.setAttachment(gzipData);
                future.channel().writeAndFlush(request);
            }
           future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

 

}