package io.netty.example.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerBusinessHandler extends ChannelInboundHandlerAdapter {
    static final String MODULE_NAME = "[Sever]:";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(MODULE_NAME + ctx.channel().id() + " : 连接建立成功 ");
        //ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg != null) {
            Msg m = (Msg) msg;
            ctx.writeAndFlush(doSomething(m));
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private Msg doSomething(Msg req) {
        try {
            System.out.println(req);
            Thread.sleep(1000 * 1000);
            String response = MODULE_NAME + req.getMsg();
            Msg resp = new Msg(response);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
