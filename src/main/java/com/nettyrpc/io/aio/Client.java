package com.nettyrpc.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;


/**
 * 
 * @author lpz
 *
 */
public class Client implements Runnable {  
  
    private AsynchronousSocketChannel channel;  
  
    /**
     * Opens an asynchronous socket channel.
     * @throws IOException
     */
    public Client() throws IOException {  
        channel = AsynchronousSocketChannel.open();  
    }  
  
    /**
     * Connects this channel. 
     */
    public void connect() {  
        channel.connect(new InetSocketAddress("127.0.0.1", 8379));  
    }  
  
    /**
     * 发送数据
     * @param data
     */
    public void write(String data) {  
        try {  
            channel.write(ByteBuffer.wrap(data.getBytes())).get();  
            read();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /**
     * 获取服务端返回的值
     */
    public void read() {  
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        try {  
            channel.read(buffer).get();  
            buffer.flip();  
            byte[] bytes = new byte[buffer.remaining()];  
            buffer.get(bytes);
            
            String data = new String(bytes, "UTF-8").trim();  
            System.out.println(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public void run() {  
        while (true) {  
  
        }  
    }  
  
    public static void main(String[] args) {  
        try {  
            Client c1 = new Client();  
            Client c2 = new Client();  
            Client c3 = new Client();  
            c1.connect();  
            c2.connect();  
            c3.connect();  
  
            new Thread(c1).start();  
            new Thread(c2).start();  
            new Thread(c3).start();  
  
            Thread.sleep(1000);  
  
            c1.write("c1 aaa");  
            c2.write("c2 bbbb");  
            c3.write("c3 ccccc");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}