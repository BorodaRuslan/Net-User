package org.example.app;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ServerApp {


    public static void main(String[] args) {
        // "bossGroup" -> пул отвечает за подключающихся клиентов (Инициализация)
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        // "workerGroup" -> пул отвечает за работу обработку данных.
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);

        try {
            // "serverBootstrap" -> предназначен для настройки нашего сервера.
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)       //  <-Подключаем два потока.
                    .channel(NioServerSocketChannel.class)     // <- Подключаем канал/
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // Инициализация клиента.
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // "Piplaine" -> можно представить как конвейер.
                            // Для каждого клиента, конвейер будет свой.
                            socketChannel.pipeline().addLast(new MainHandler());

                        }
                    });
            /*
            Запускаем сервер и передаем ему порт по которому он будет работать.

            Главная цель класса ChannelFuture состоит в том,
            чтобы предоставить механизм для отслеживания результатов асинхронных операций,
            связанных с сетевыми каналами (Channel) в Netty.
            */
            ChannelFuture future = serverBootstrap.bind(8080).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();        // <-Закрываем потоки
            workerGroup.shutdownGracefully();
        }

    }
}
