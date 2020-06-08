package io.netty.example.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8008"));

    //创建ChannelInitializer,用于初始化NioSocketChannel的Pipeline
    //每一个Channel都会有自己的ChannelPipeline。Channel的子接口在ChannelPipeline上传递完成业务处理
    //ChannelHandler分为InBoundHandler和OutBoundHandler
    static final DefaultEventLoopGroup businessWorkes = new DefaultEventLoopGroup(16);
    static final ChannelInitializer childHandlers = new ChannelInitializer() {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            ChannelPipeline p = ch.pipeline();
            p.addLast(businessWorkes, new MsgDecode(Integer.MAX_VALUE, 0, 4));
            p.addLast(businessWorkes, new MsgEncode());
            p.addLast(businessWorkes, new ServerBusinessHandler());
        }
    };

    public static void main(String[] args) throws Exception {
        //创建Boss线程池和Worker线程池
        //Boss线程池的Thread数设置为1;因为NioServerSocketChannel只会注册到NioEventLoopGroup内的其中一个NioEventLoop
        //然后由注册到的NioEventLoop进行Accept操作，获取建立好的连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //设置Worker线程数量。默认为CPU*2;从NioServerSocketChannel获取的连接通过Chooser选择NioEventLoop进行绑定
        //一个NioEventLoop可以绑定多个Channel，一个Channel只能分配到一个NioEventLoop
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    //因为是服务端所以创建的NioServerSocketChannel，如果是Client端则是NioSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //Option设置的NioServerSocketChannel上的配置
                    .option(ChannelOption.SO_BACKLOG,100)
                    //设置NioServerSocketChannel的Handler
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //设置获取的SocketChannel的Handler
                    .childHandler(childHandlers)
                    //设置获取的SocketChannel的配置
                    .childOption(ChannelOption.TCP_NODELAY,true).
                    childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK,1024*64);

            ChannelFuture f = b.bind(PORT).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
