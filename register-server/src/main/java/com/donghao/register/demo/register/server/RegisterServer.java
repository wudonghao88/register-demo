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
        while (true) {
            try {
                Thread.sleep(30 * 1000L);
            } catch (InterruptedException e) {

            }
        }
    }
}
