package com.nettyrpc.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;



/**
 * 客户端
 * @author lpz
 *
 */
public class Client {
    
    private static int PORT = 8379; 
    
    public static void main(String[] args) {
        // Creates a socket address from a hostname and a port number. 
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", PORT);  
        SocketChannel sChannel = null;
        
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        try {  
            //1.打开通道  
            sChannel = SocketChannel.open();  
            //2.建立连接, Connects this channel's socket. 
            sChannel.connect(socketAddress);
            
            // 持续监听console的输入，然后请求server
            while (true) {  
                byte[] bytes = new byte[1024];  
                System.in.read(bytes);  
                //3.把输入的数据放入buffer缓冲区  
                buffer.put(bytes);  
                //4.复位操作  
                buffer.flip();
                
                //5.将buffer的数据写入通道  
                sChannel.write(buffer);  
                //清空缓冲区中的数据  
                buffer.clear();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(sChannel != null) {  
                try {  
                    sChannel.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
}