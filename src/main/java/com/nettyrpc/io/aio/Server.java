package com.nettyrpc.io.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 在NIO的基础上引入了异步通道的概念，并提供了异步文件和异步套接字通道的实现，从而在真正意义上实现了异步非阻塞，之前的NIO只是"非阻塞"而并非异步。
 * AIO不需要通过对多路复用器对注册的通道进行轮询操作即可实现异步读写，从而简化NIO编程模型。
 * ①AsynchronousServerSocketChannel
 * ②AsynchronousSocketChannel
 * 
 * Server端
 * 
 * @author lpz
 *
 */
public class Server {
    
    //线程池  
    private ExecutorService executorService;  
    //线程组  
    private AsynchronousChannelGroup channelGroup;  
    //服务器通道  
    public AsynchronousServerSocketChannel channel;
    
    /**
     * 
     * @param port
     */
    public Server(int port) {  
        try {  
            //创建线程池  
            executorService  = Executors.newCachedThreadPool();  
            //创建线程组  
            channelGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);  
            //创建服务器通道  
            channel = AsynchronousServerSocketChannel.open(channelGroup);  
            //绑定地址  
            channel.bind(new InetSocketAddress(port));  
            System.out.println("server start, port：" + port);
            
            channel.accept(this, new ServerCompletionHandler());  
            Thread.sleep(Integer.MAX_VALUE);
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    
    public static void main(String[] args) {  
        Server server = new Server(8379);  
    }
    
    
    
}