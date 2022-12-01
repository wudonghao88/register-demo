package com.donghao.register.demo.register.server.controller;

import java.util.Map;

import com.donghao.register.demo.register.common.constants.ResponseStatusConstants;
import com.donghao.register.demo.register.common.pojo.*;
import com.donghao.register.demo.register.server.pojo.Registry;
import com.donghao.register.demo.register.server.pojo.SelfprotectionPolicy;
import com.donghao.register.demo.register.server.tools.HeartbeatMessuredRate;

/**
 * 这个controller是接收 register-client发送过来的请求
 * 在springcloud Eureka中组件是jersey。
 * jersey:restful框架，可以接收http请求
 */
public class RegisterController {
  /**
   * 注册中心
   */
  private Registry registry = Registry.getInstance();

  /**
   * 心跳监控计数器
   */
  private HeartbeatMessuredRate heartbeatRate = HeartbeatMessuredRate.getInstance();

  /**
   * 自我保护机制
   */
  private SelfprotectionPolicy selfprotectionPolicy = SelfprotectionPolicy.getInstance();

  /**
   * 注册服务
   *
   * @param request request
   * @return 返回结果
   */
  public RegisterResponse register(RegisterRequest request) {
    RegisterResponse registerResponse = new RegisterResponse();
    try {
      ServiceInstance serviceInstance = new ServiceInstance();
      serviceInstance.setServiceName(request.getServiceName());
      serviceInstance.setIp(request.getIp());
      serviceInstance.setHostname(request.getHostName());
      serviceInstance.setPort(request.getPort());
      serviceInstance.setServiceInstanceId(request.getServiceInstanceId());
      registry.register(serviceInstance);
      registerResponse.setStatus(ResponseStatusConstants.SUCCESS);
      // 增加自我保护机制期望心跳次数，并根据最新的期望心跳次数更新阈值
      // todo 其实可以把对方的心跳间隔时间也传过来这样计算之后增加才是正解
      synchronized (SelfprotectionPolicy.class) {
        SelfprotectionPolicy selfprotectionPolicy = SelfprotectionPolicy.getInstance();
        selfprotectionPolicy.setExpectedHeartbeatRate(selfprotectionPolicy.getExpectedHeartbeatRate() + 2);
        // 比率也可以设置为yml参数
        selfprotectionPolicy.setExpectedHeartbeatThreshold((long) (selfprotectionPolicy.getExpectedHeartbeatRate() * 0.85));
      }
    } catch (Exception e) {
      registerResponse.setStatus(ResponseStatusConstants.FAILURE);
    }
    return registerResponse;
  }

  /**
   * 心跳续约
   *
   * @param request request
   * @return 返回结果
   */
  public HeartbeatResponse heartbeat(HeartbeatRequest request) {
    System.out.println("心跳操作：服务实例：" + request.getServiceInstanceId() + "心跳请求续约");
    HeartbeatResponse response = new HeartbeatResponse();
    try {
      ServiceInstance serviceInstance = registry.getServiceInstance(request.getServiceName(), request.getServiceInstanceId());
      if (serviceInstance == null) {
        response.setStatus(ResponseStatusConstants.FAILURE);
        return response;
      }
      // 续约
      serviceInstance.renew();
      response.setStatus(ResponseStatusConstants.SUCCESS);
      heartbeatRate.increment();
    } catch (Exception e) {
      response.setStatus(ResponseStatusConstants.FAILURE);
      return response;
    }
    return response;
  }

  /**
   * 拉取注册表信息
   * 
   * @return 注册表信息
   */
  public Map<String, Map<String, ServiceInstance>> fetchServiceRegistry() {
    return registry.getRegistry();
  }

  /**
   * 服务下线接口
   * 
   * @param serviceName serviceName
   * @param serviceInstanceId serviceInstanceId
   */
  public void cancel(String serviceName, String serviceInstanceId) {
    registry.remove(serviceName, serviceInstanceId);
    // 增加自我保护机制期望心跳次数，并根据最新的期望心跳次数更新阈值
    // todo 其实可以把对方的心跳间隔时间也传过来这样计算之后增加才是正解
    synchronized (SelfprotectionPolicy.class) {
      SelfprotectionPolicy selfprotectionPolicy = SelfprotectionPolicy.getInstance();
      selfprotectionPolicy.setExpectedHeartbeatRate(selfprotectionPolicy.getExpectedHeartbeatRate() - 2);
      // 比率也可以设置为yml参数
      selfprotectionPolicy.setExpectedHeartbeatThreshold((long) (selfprotectionPolicy.getExpectedHeartbeatRate() * 0.85));
    }
  }
}
