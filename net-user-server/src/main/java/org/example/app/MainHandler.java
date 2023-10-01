package org.example.app;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MainHandler extends ChannelInboundHandlerAdapter {

    /*
    "Handler" (обработчик) - это ключевой компонент, который используется для обработки событий,
    связанных с сетевым вводом и выводом.
    */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected!");
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Получаем от пользователя сообщение (поток байтов)
        ByteBuf buf = (ByteBuf) msg;
        // Пока в буфере есть байты, мы их вытягиваем и преобразовываем в char.
        while (buf.readableBytes() > 0){
            System.out.print((char) buf.readByte());
        }
        // Обязательно нужно отчистить буфер!
        buf.release();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
