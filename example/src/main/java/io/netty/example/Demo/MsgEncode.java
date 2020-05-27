package io.netty.example.Demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.StringUtil;

/**
 * @Descriprion:
 * @Author:wuxiaoguang@58.com
 * @Date：created in 2020/5/27
 */
public class MsgEncode extends MessageToByteEncoder<Msg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Msg msg, ByteBuf out) throws Exception {
        if (null == msg || StringUtil.isNullOrEmpty(msg.getMsg())) {
            return;
        }

        byte[] bytes = msg.getMsg().getBytes("UTF-8");
        if (msg.getLength() == null) {
            msg.setLength(bytes.length);
        }

        out.writeInt(msg.getLength());
        out.writeBytes(bytes);
    }
}
