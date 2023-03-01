package org.example.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.discard.DiscardServerHandler;

public class TimeServer {
    private final int port;

    public TimeServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        var eventLoopGroup = new NioEventLoopGroup();
        var eventLoopGroup2 = new NioEventLoopGroup();

        try {
            var serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(eventLoopGroup, eventLoopGroup2)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel
                                    .pipeline()
                                    .addLast(new TimeServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            var channelFuture = serverBootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        var port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        new TimeServer(port).run();
    }
}
