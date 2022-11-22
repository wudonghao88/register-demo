package com.donghao.register.demo.register.server.controller;

import com.donghao.register.demo.register.server.pojo.RegisterRequest;
import com.donghao.register.demo.register.server.pojo.RegisterResponse;

/**
 * 这个controller是接收 register-client发送过来的请求
 * 在springcloud Eureka中组件是jersey。
 * jersey:restful框架，可以接收http请求
 */
public class RegisterController {
    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse registerResponse = new RegisterResponse();
        try {
            registerResponse.setStatus()
        }catch (Exception e) {

        }
        return null;
    }
}
