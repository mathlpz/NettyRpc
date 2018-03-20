package com.nettyrpc.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


/**
 * IO（BIO）与NIO的区别，其本质就是阻塞和非阻塞的区别。
 * 阻塞概念：应用程序在获取网络数据的时候，如果网络传输数据很慢，那么程序就一直等着，直到传输完毕为止。
 * 非阻塞概念：应用程序直接可以获取已经准备就绪的数据，无需等待。
 * 
 * IO为同步阻塞形式，NIO为同步非阻塞形式。NIO没有实现异步，在JDK1.7之后，升级了NIO库包，支持异步非阻塞通信模型，即NIO2.0(AIO)。
 * 
 * 同步和异步：同步和异步一般是面向“操作系统”与应用程序对IO操作的层面上来区别的。
 *      ①同步时，应用程序会直接参与IO读写操作，并且应用程序会直接阻塞到某一个方法上，直到数据准备就绪（BIO）；或者采用轮询的策略实时检查数据的就绪状态，如果就绪则获取数据（NIO）。
 *      ②异步时，则所有的IO读写操作都交给操作系统处理，与应用程序没有直接关系，应用程序并不关心IO读写，当操作系统完成IO读写操作时，会向应用程序发出通知，应用程序直接获取数据即可。
 * 
 * 同步说的是Server服务端的执行方式！
 * 阻塞说的是具体的技术，接收数据的方式、状态（io、nio）。
 * 
 * NIO编程，几个概念：
 * （1）Buffer（缓冲区）
 *      在面向流的IO中，可以直接将数据写入或读取到Stream对象中。在NIO类库中，所有的数据都是用缓冲区处理的（读写）。这个数组为缓冲区提供了访问数据的读写等操作属性，如位置、容量、上限等概念
 * （2）Channel（管道、通道）
 *      Channel就像自来水管道一样，网络数据通过Channel读取和写入，通道与流的不同之处在于通道是双向的，而流只能在一个方向上移动（一个流必须是InputStream或者OutputStream的子类），
 *          而通道可以用于读、写或者二者同时进行，最关键的是可以和多路复用器集合起来，有多种的状态位，方便多路复用器去识别。
 *      通道分为两大类：一类是用于网络读写的SelectableChannel，另一类是用于文件操作的FileChannel，我们使用的SocketChannel和ServerSocketChannel都是SelectableChannel的子类。
 * （3）Selector（选择器、多路复用器）
 *      Selector会不断的轮询注册在其上的通道（Channel），如果某个通道发生了读写操作，这个通道就处于就绪状态，会被Selector轮询出来，然后通过SelectionKey可以取得就绪的Channel集合，从而进行后续的IO操作。
 *  事件状态：SelectionKey.OP_CONNECT、SelectionKey.OP_ACCEPT、SelectionKey.OP_READ、SelectionKey.OP_WRITE
 * 
 * Buffer、Channel、Selector的一个入门的小例子：
 * 
 * @author lpz
 *
 */
public class Server implements Runnable {  
  
    private Selector selector;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    
    private static int PORT = 8379; 
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        // 启动server线程
        new Thread(new Server(PORT)).start();
        
    } 
    
    public Server(int port) {
        try {  
            //1 打开多复用器
            selector = Selector.open();
            
            //2 打开服务器通道  
            ServerSocketChannel ssChannel = ServerSocketChannel.open();  
            //3 设置服务器通道为非阻塞方式  
            ssChannel.configureBlocking(false);  
            //4 绑定地址  
            ssChannel.bind(new InetSocketAddress(port));
            
            //5 把服务器通道注册到多路复用选择器上，并监听阻塞状态  
            ssChannel.register(selector, SelectionKey.OP_ACCEPT);
            
            System.out.println("Server start, port：" + port);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public void run() {
        
        // 循环监听！！
        while (true) {  
            try {  
                //1 必须让多路复用选择器, 开始监听  
                selector.select();  
                //2 返回所有已经注册到多路复用选择器上的通道的SelectionKey  
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();  
                //3 遍历keys  
                while (keys.hasNext()) {  
                    SelectionKey key = keys.next();  
                    keys.remove();  
                    if(key.isValid()) { //如果key的状态是有效的  
                        if(key.isAcceptable()) { //如果key是阻塞状态，则调用accept()方法  
                            accept(key);  
                        }  
                        if(key.isReadable()) { //如果key是可读状态，则调用read()方法  
                            read(key);  
                        }  
                    }  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /**
     * SelectionKey status is isAcceptable阻塞状态，，注册到多路复用选择器上，并设置读取标识 
     * 如果是ACCEPT状态，说明是新的客户端接入，则调用accept方法接收新的客户端。
     * @param key
     */
    private void accept(SelectionKey key) {  
        try {  
            //1 获取服务器通道  
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();  
            //2 执行阻塞方法  
            SocketChannel sc = ssc.accept();  
            //3 设置阻塞模式为非阻塞  
            sc.configureBlocking(false);  
            //4 注册到多路复用选择器上，并设置读取标识  
            sc.register(selector, SelectionKey.OP_READ);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /**
     * isReadable可读状态，，读取数据到buffer并输出
     * @param key
     */
    private void read(SelectionKey key) {  
        try {  
            //1 清空缓冲区中的旧数据  
            buffer.clear();  
            //2 获取之前注册的SocketChannel通道  
            SocketChannel sc = (SocketChannel) key.channel();  
            //3 将sc中的数据放入buffer中  
            int count = sc.read(buffer);  
            if(count == -1) { // == -1表示通道中没有数据  
                key.channel().close();  
                key.cancel();  
                return;  
            }  
            //读取到了数据，将buffer的position复位到0  
            buffer.flip();  
            byte[] bytes = new byte[buffer.remaining()];  
            //将buffer中的数据写入byte[]中  
            buffer.get(bytes);  
            String body = new String(bytes).trim();
            
            System.out.println("Server：" + body);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    
}