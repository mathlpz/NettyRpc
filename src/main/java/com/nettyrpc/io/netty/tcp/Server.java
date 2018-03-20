package com.nettyrpc.io.netty.tcp;
public class Server {

 

    public Server(int port) {

        EventLoopGroup bossGroup = newNioEventLoopGroup();

        EventLoopGroup workerGroup = newNioEventLoopGroup();

        try {

            ServerBootstrap bootstrap = newServerBootstrap();

            bootstrap.group(bossGroup, workerGroup)

                   .channel(NioServerSocketChannel.class)

                    .handler(newLoggingHandler(LogLevel.INFO))

                    .childHandler(newChannelInitializer<SocketChannel>() {

                        @Override

                        protected voidinitChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());

                            socketChannel.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());

                           socketChannel.pipeline().addLast(new ServerHandler());

                        }

                    })

                    .option(ChannelOption.SO_BACKLOG,1024)

                   .option(ChannelOption.SO_RCVBUF, 32 * 1024)

                   .option(ChannelOption.SO_SNDBUF, 32 * 1024)

                   .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = bootstrap.bind(port).sync();

           future.channel().closeFuture().sync();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            bossGroup.shutdownGracefully();

            workerGroup.shutdownGracefully();

        }

    }

 

    public static void main(String[] args) {

        new Server(8765);

    }

}