package com.nettyrpc.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerHandler implements Runnable {  
  
    private Socket socket;  
  
    public ServerHandler(Socket socket) {
        System.out.println("ServerHandler()：" + socket.getInetAddress());
        this.socket = socket;  
    }  
  
    @Override  
    public void run() {  
        BufferedReader bufferedReader = null;  
        PrintWriter printWriter = null;  
        try {  
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            printWriter = new PrintWriter(socket.getOutputStream(), true);  
  
            while (true) {  
                String info = bufferedReader.readLine();  
                System.out.println("客户端发送的消息000：" + info);
                if(info == null)  
                    break;
                System.out.println("客户端发送的消息：" + info);
                
                // server 返回响应客户端消息
                printWriter.println("printWriter, 服务器端响应了客户端请求....");  
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