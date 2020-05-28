package io.netty.example.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Descriprion:
 * @Author:wuxiaoguang@58.com
 * @Date：created in 2020/5/27
 */
public class MsgDecode extends LengthFieldBasedFrameDecoder {

    public MsgDecode(
            int maxFrameLength,
            int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, 0, 0);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf b = (ByteBuf) super.decode(ctx, in);
        if (b == null) {
            return null;
        }

        Msg msg = new Msg();
        int length = b.readInt();
        msg.setLength(length);
        byte[] msgByte = new byte[length];
        b.readBytes(msgByte);
        String m = new String(msgByte, "UTF-8");
        msg.setMsg(m);
        return msg;
    }
}
