package com.nettyrpc.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * ①创建ServerSocket对象，绑定监听端口

②通过accept()方法监听客户端请求

③建立连接后，通过输入流读取客户端发送的请求信息

④通过输出流向客户端发送响应信息

⑤关闭相关资源
 * 
 * @author lpz
 *
 */
public class Server {
    
    private static int PORT = 8379;
    
    
    
    public static void main(String[] args) {
        ExecutorService executor = Executors. newCachedThreadPool();
        // new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
        ServerSocket serverSocket = null;  
        try {  
            serverSocket = new ServerSocket(PORT);
            System.out.println("服务器端启动了....");
            
            // 阻塞，循环监听port端口。收到请求后，创建线程处理
            while (true) {
                //进行阻塞  
                Socket socket = serverSocket.accept();
                
                //启动一个线程来处理客户端请求  
//                new Thread(new ServerHandler(socket)).start();
                executor.execute(new ServerHandler(socket));
            }
            
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(serverSocket != null){  
                try {  
                    serverSocket.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            serverSocket = null;  
        }
    }
    
    
} 