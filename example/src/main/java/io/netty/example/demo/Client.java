package io.netty.example.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

public class Client {
    static final String HOST = "127.0.0.1";
    static final int PORT = 8008;

    private static Bootstrap bootstrap;
    private static Channel channel = null;


    static final ChannelInitializer childHandlers = new ChannelInitializer() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(new MsgDecode(Integer.MAX_VALUE, 0, 4));
            p.addLast(new MsgEncode());
            p.addLast(new ClientBusinessHandler());
        }
    };

    private static ChannelFutureListener channelFutureListener = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                System.out.println("连接建立成功");
                //连接成功
                channel = future.channel();
            } else {
                future.channel().eventLoop().schedule(new Runnable() {
                    @Override
                    public void run() {
                        doConnect();
                    }
                }, 3, TimeUnit.SECONDS);
            }
        }
    };

    public static synchronized void init() {
        //创建NioEventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group)//设置workGroup
                    .channel(NioSocketChannel.class)//设置channel,然后通过工厂创建Channel
                    .option(ChannelOption.TCP_NODELAY, true)//设置option
                    //设置handler在Channel中组成职责链，负责编码、解码和业务处理等[开发中最核心的部分]
                    .handler(childHandlers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void doConnect() {
        ChannelFuture future = bootstrap.connect(HOST, PORT);
        future.addListener(channelFutureListener);
    }

    public static boolean isActive() {
        if(channel==null){
            System.out.println("Channel is null");
            return false;
        }

        if(!channel.isOpen()){
            System.out.println("Channel is not open");
            return false;
        }

        if(!channel.isActive()){
           System.out.println("Channel is not Active");
            return false;
        }
        return true;
    }

 /*   public static boolean isReadAble(){
        return channel.isActive();
    }
*/

    public static void sendMsg(Msg msg) {
        channel.writeAndFlush(msg);
    }
}
