package com.donghao.register.demo.register.server.controller;

import com.donghao.register.demo.register.common.constants.ResponseStatusConstants;
import com.donghao.register.demo.register.server.pojo.*;

/**
 * 这个controller是接收 register-client发送过来的请求
 * 在springcloud Eureka中组件是jersey。
 * jersey:restful框架，可以接收http请求
 */
public class RegisterController {

    private Registry registry = Registry.getInstance();

    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            ServiceInstance serviceInstance = new ServiceInstance();
            serviceInstance.setServiceName(request.getServiceName());
            serviceInstance.setIp(request.getIp());
            serviceInstance.setHostName(request.getHostName());
            serviceInstance.setPort(request.getPort());
            serviceInstance.setServiceInstanceId(request.getServiceInstanceId());
            serviceInstance.setLease(new Lease());
            registry.register(serviceInstance);
            registerResponse.setStatus(ResponseStatusConstants.SUCCESS);
        } catch (Exception e) {
            registerResponse.setStatus(ResponseStatusConstants.FAILURE);
        }
        return registerResponse;
    }
}
