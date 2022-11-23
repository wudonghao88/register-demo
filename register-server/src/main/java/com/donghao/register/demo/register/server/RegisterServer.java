package com.donghao.register.demo.register.server;

import com.donghao.register.demo.register.server.controller.RegisterController;
import com.donghao.register.demo.register.server.monitor.ServiceAliveMonitor;
import com.donghao.register.demo.register.server.pojo.RegisterRequest;

import java.util.UUID;

/**
 * 服务注册中心
 */
public class RegisterServer {
    public static void main(String[] args) {
        RegisterController controller = new RegisterController();
        //模拟发起注册请求
        RegisterRequest request = new RegisterRequest();
        request.setServiceName("inventory-service");
        request.setHostName("inventory-service-01");
        request.setIp("192.168.31.208");
        request.setPort(9000);
        request.setServiceInstanceId(UUID.randomUUID().toString().replaceAll("-", ""));
        controller.register(request);
        //服务器实例监测
        ServiceAliveMonitor serviceAliveMonitor = new ServiceAliveMonitor();
        serviceAliveMonitor.start();
        /*
        一般来说register-server不会只有一个main线程作为工作线程
        而是作为一个web工程，部署到一个web服务器理
        它最核心的工作线程，就是专门用于接收和处理register-client发送过来的那些工作线程
        如果说工作线程都停止了，namedeam线程就会跟着jvm进程一起退出
        由于当前项目不是web线程，于是使用了main线程while(ture)保障当前工作线程不退出
         */
        while (true) {
            try {
                Thread.sleep(30 * 1000L);
            } catch (InterruptedException e) {

            }
        }
    }
}
