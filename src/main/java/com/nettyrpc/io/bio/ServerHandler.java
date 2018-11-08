package com.nettyrpc.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler implements Runnable {

	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
  
    private Socket socket;  
  
    public ServerHandler(Socket socket) {
		LOGGER.info(
				"ServerHandler() constructor received socket, localPort:{}, localAddress:{}, channel:{}, inetAddress:{} ",
				socket.getLocalPort(), socket.getLocalAddress(),
				socket.getChannel(), socket.getInetAddress());
        this.socket = socket;  
    }  
  
    @Override  
    public void run() {  
        BufferedReader bufferedReader = null;  
        PrintWriter printWriter = null;  
		try {
			// 读socket数据
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 返回socket数据
            printWriter = new PrintWriter(socket.getOutputStream(), true);  
  
            while (true) {  
                String info = bufferedReader.readLine();  
                if(info == null)  
                    break;
				LOGGER.info("客户端发送的消息，readLine：" + info);
                
                // server 返回响应客户端消息
				printWriter.println("printWriter socket outputStream, 服务器端响应了客户端请求....");
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(bufferedReader != null){  
                try {  
                    bufferedReader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            if(printWriter != null){  
                try {  
                    printWriter.close();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
            if(socket != null){  
                try {  
                    socket.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
            socket = null;  
        }  
    }  
}  