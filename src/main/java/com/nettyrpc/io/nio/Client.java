package com.nettyrpc.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 客户端
 * @author lpz
 *
 */
public class Client {

	private static Logger LOGGER = LoggerFactory.getLogger(Client.class);
    
    private static int PORT = 8379; 
    

    public static void main(String[] args) {
        // Creates a socket address from a hostname and a port number. 
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", PORT);  
		// 管道管理器
		Selector selector;
		// 客户端通道
        SocketChannel socketChannel = null;
        
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        try {  
			// 获得通道管理器
			selector = Selector.open();

            //1.打开通道  
            socketChannel = SocketChannel.open();  
			socketChannel.configureBlocking(false);

            //2.建立连接, Connects this channel's socket. 
			boolean connect = socketChannel.connect(socketAddress);
			// 为该通道注册SelectionKey.OP_CONNECT事件
			socketChannel.register(selector, SelectionKey.OP_CONNECT);

			LOGGER.info("客户端启动. Client SocketChannel connect success: {}. socketAddress:{}, localAddress:{} ", connect,
					socketAddress, socketChannel.getLocalAddress());

			// 轮询访问selector
			while (true) {
				// 选择注册过的io操作的事件(第一次为SelectionKey.OP_CONNECT)
				selector.select();
				Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
				while (ite.hasNext()) {
					SelectionKey key = ite.next();
					// 删除已选的key，防止重复处理
					ite.remove();
					if (key.isConnectable()) {
						SocketChannel channel = (SocketChannel) key.channel();

						// 如果正在连接，则完成连接
						if (channel.isConnectionPending()) {
							channel.finishConnect();
						}

						channel.configureBlocking(false);
						// 向服务器发送消息
						channel.write(ByteBuffer.wrap(new String("send message to server...").getBytes()));

						// 连接成功后，注册接收服务器消息的事件
						channel.register(selector, SelectionKey.OP_READ);
						LOGGER.info("客户端连接成功");
					} else if (key.isReadable()) { // 有可读数据事件。
						SocketChannel channel = (SocketChannel) key.channel();

						channel.read(buffer);
						byte[] data = buffer.array();
						String message = new String(data);

						LOGGER.info("recevie message from server:, size:" + buffer.position() + " msg: " + message);
//	                    ByteBuffer outbuffer = ByteBuffer.wrap(("client.".concat(msg)).getBytes());
//	                    channel.write(outbuffer);
					}
				}
			}

//            // 持续监听console的输入，然后请求server
//            while (true) {  
//                byte[] bytes = new byte[1024];  
//                System.in.read(bytes);  
//                //3.把输入的数据放入buffer缓冲区  
//                buffer.put(bytes);  
//                //4.复位操作  
//                buffer.flip();
//                
//                //5.将buffer的数据写入通道  
//                socketChannel.write(buffer);  
//                //清空缓冲区中的数据  
//                buffer.clear();  
//            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if(socketChannel != null) {  
                try {  
                    socketChannel.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
}