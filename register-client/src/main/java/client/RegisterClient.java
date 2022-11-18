package client;

import java.util.UUID;

/**
 * 在服务上被创建和启动,负责和register-server进行通信
 *
 * @author donghao.wu
 */
public class RegisterClient {
    private String serviceInstanceId;

    public RegisterClient() {
        this.serviceInstanceId = UUID.randomUUID().toString().replace("-", "");
    }

    public void start() {
        /*
        一旦启动组件之后:
        1.想register-server发送请求,注册这个服务
        2.注册成功后,开启另一个进程发送信条
         */
        RegisterWorker registerWorker = new RegisterWorker(serviceInstanceId);
        registerWorker.start();
    }
}
