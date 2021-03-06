package com.nettyrpc.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


/**
 * 
 * @author lpz
 *
 */
public class ServerCompletionHandler 
        implements CompletionHandler<AsynchronousSocketChannel, Server> {
    
    @Override  
    public void completed(AsynchronousSocketChannel channel, Server server) {  
        //当有下一个客户端接入的时候，直接调用Server的accept方法. 这样反复执行下去，保证多个客户端都可以阻塞  
        server.channel.accept(server, this);  
        read(channel);  
    }  
    
    /**
     * 读取client发送的消息
     * @param channel
     */
    private void read(AsynchronousSocketChannel channel) {  
        //读取数据  
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        channel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override  
            public void completed(Integer resultSize, ByteBuffer attachment) {  
                attachment.flip();  
                System.out.println("Server->" + "收到客户端发送的数据长度为：" + resultSize);  
                String data = new String(buffer.array()).trim();  
                System.out.println("Server->" + "收到客户端发送的数据为：" + data);
                
                String response = "服务器端响应了客户端。。。。。。";  
                write(channel, response);  
            }  
  
            @Override  
            public void failed(Throwable exc, ByteBuffer attachment) {  
                exc.printStackTrace();  
            }  
        });  
    }  
    
    /**
     * 返回给client
     * @param channel
     * @param response
     */
    private void write(AsynchronousSocketChannel channel, String response) {  
        try {  
            ByteBuffer buffer = ByteBuffer.allocate(1024);  
            buffer.put(response.getBytes());  
            buffer.flip();  
            channel.write(buffer).get();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public void failed(Throwable exc, Server attachment) {  
        exc.printStackTrace();  
    }
    
    
} 