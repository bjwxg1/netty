package io.netty.example.demo;

/**
 * @Descriprion:
 * @Author:wuxiaoguang@58.com
 * @Date：created in 2020/5/27
 */
public class ClientDemo {

    public static void main(String[] args) throws Exception {
        Client.init();
        Client.doConnect();
        int i = 0;
        while (true) {
            if (Client.isActive()) {
                String msg = "This is test Msg" + i;
                Client.sendMsg(new Msg(msg));
                i++;
                System.out.println(i);
                break;
            }
        }
        Thread.sleep(100000);
    }
}
