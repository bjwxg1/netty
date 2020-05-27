/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.Demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientBusinessHandler extends ChannelInboundHandlerAdapter {
    static final String MODULE_NAME = "[ClientHandler]";

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(MODULE_NAME + ":" + ctx.channel().id() + " : 连接建立成功 ");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg != null) {
            Msg m = (Msg) msg;
            System.out.println(MODULE_NAME + ctx.channel().id() + " : " + m.getMsg());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        System.err.println(MODULE_NAME + ":" + ctx.channel().id() + " : 异常");
        cause.printStackTrace();
        ctx.close();
        Client.doConnect();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(MODULE_NAME + ":" + ctx.channel().id() + " : Inactive");
        ctx.close();
        Client.doConnect();
    }
}
