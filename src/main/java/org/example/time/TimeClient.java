package org.example.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        var host = "localhost";
        var port = 8080;
        try (var eventLoopGroup = new NioEventLoopGroup()) {
            var bootStrap = new Bootstrap();
            bootStrap
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel
                                    .pipeline()
                                    .addLast(new TimeClientHandler());
                        }
                    });

            var channelFuture = bootStrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        }
    }
}
