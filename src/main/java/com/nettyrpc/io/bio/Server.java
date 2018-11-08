package com.nettyrpc.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
    
    private static int PORT = 8379;
    
    
    
    public static void main(String[] args) {
        ExecutorService executor = Executors. newCachedThreadPool();
        // new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxSize, 120L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
        ServerSocket serverSocket = null;  
        try {  
            serverSocket = new ServerSocket(PORT);
			LOGGER.info("服务器端启动了....");
            
            // 阻塞，循环监听port端口。收到请求后，创建线程处理
            while (true) {
                //进行阻塞  
				// 采用BIO通信模型的服务端，通常由一个独立的Acceptor线程负责监听客户端的连接，它接收到客户端连接请求之后为每个客户端创建一个新的线程进行链路处理没，
				// 处理完成后，通过输出流返回应答给客户端，线程销毁。
				// 典型的一请求一应答通宵模型。
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