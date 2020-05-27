package io.netty.example.Demo;

import java.io.Serializable;

/**
 * @Descriprion:
 * @Author:wuxiaoguang@58.com
 * @Date：created in 2020/5/27
 */
public class Msg implements Serializable {
    private Integer length;
    private String msg;

    public Msg() {
    }

    public Msg(String msg) {
        this.msg = msg;
    }

    public Msg(String msg, Integer length) {
        this.msg = msg;
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "[ length=" + length + "; msg=" + msg + "]";
    }
}
