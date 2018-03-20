package com.nettyrpc.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * 
 * ①创建Socket对象，指明需要连接的服务器的地址和端口号

②连接建立后，通过输出流向服务器端发送请求信息

③通过输入流获取服务器响应信息

④关闭响应资源
 * 
 * @author lpz
 *
 */
public class Client {
    
    private static int PORT = 8379;  
    private static String IP = "127.0.0.1";  
    
    public static void main(String[] args) {  
        BufferedReader bufferedReader = null;  
        PrintWriter printWriter = null;  
        Socket socket = null;  
        try {  
            socket = new Socket(IP, PORT);  
            printWriter = new PrintWriter(socket.getOutputStream(), true);  
            printWriter.println("woshikehuduan, 客户端请求了服务器....");
            
            // 读取server返回的信息
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            String response = bufferedReader.readLine();  
            System.out.println("Client recived：" + response);  
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
            } else {  
                socket = null;  
            }  
        }  
    }  
} 