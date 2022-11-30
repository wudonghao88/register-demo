package com.donghao.register.demo.register.client.tools;

import static com.donghao.register.demo.register.common.constants.ResponseCodeConstants.FAILURE_TWO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.donghao.register.demo.register.common.constants.ResponseStatusConstants;

import com.donghao.register.demo.register.common.pojo.HeartbeatRequest;
import com.donghao.register.demo.register.common.pojo.RegisterRequest;
import com.donghao.register.demo.register.common.pojo.RegisterResponse;
import com.donghao.register.demo.register.common.pojo.ServiceInstance;
import okhttp3.*;

/**
 * 负责发送http请求的组件
 *
 * @author donghao.wu
 */
public class HttpSender {
  private static final String URL = "";

  public RegisterResponse register(RegisterRequest req) {
    /*
     * 构造请求,放入示例名称,服务名称,ip地址
     * 端口号等
     */
    System.out.println(this.getClass().getSimpleName() + "服务实例发送请求进行注册");
    OkHttpClient client = new OkHttpClient();
    RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONObject.toJSONString(req));
    Request request = new Request.Builder().url(URL).post(body).build();
    RegisterResponse res = new RegisterResponse();
    try {
      Response execute = client.newCall(request).execute();
      res.setStatus(execute.isSuccessful() ? ResponseStatusConstants.SUCCESS : ResponseStatusConstants.FAILURE);
      res.setCode(execute.code());
    } catch (IOException e) {
      e.printStackTrace();
      // 直接打message
      System.out.println(e.getMessage());
      res.setCode(FAILURE_TWO);
      res.setStatus(ResponseStatusConstants.FAILURE);
      res.setMessage(e.getMessage());
      // todo 可以增加重试逻辑
      /*
       * 重试逻辑设计
       * 异步
       * 内循环 3次 每秒重试一次 3次都失败则写入内存队列
       * 定时任务:
       * 每分钟执行一次;执行15次后,若内存队列无内容调整调整为半小时执行一次
       */
    }
    System.out.println(res);
    return res;
  }

  /**
   * 发送心跳
   *
   * @param req 入参
   * @return http心跳请求返回值
   */
  public RegisterResponse heatbeat(HeartbeatRequest req) {
    /*
     * 发送请求进行心跳
     * 构造请求,放入示例名称,服务名称,ip地址
     * 端口号等
     */
    System.out.println(this.getClass().getSimpleName() + "服务实例发送请求进行注册");
    OkHttpClient client = new OkHttpClient();
    RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONObject.toJSONString(req));
    Request request = new Request.Builder().url(URL).post(body).build();
    RegisterResponse res = new RegisterResponse();
    try {
      System.out.println("发送请求");
      Response execute = client.newCall(request).execute();
      res.setStatus(execute.isSuccessful() ? ResponseStatusConstants.SUCCESS : ResponseStatusConstants.FAILURE);
      res.setCode(execute.code());
    } catch (IOException e) {
      e.printStackTrace();
      // 直接打message
      System.out.println(e.getMessage());
      res.setCode(FAILURE_TWO);
      res.setStatus(ResponseStatusConstants.FAILURE);
      res.setMessage(e.getMessage());
      // todo 可以增加重试逻辑
      /*
       * 重试逻辑设计
       * 异步
       * 内循环 3次 每秒重试一次 3次都失败则写入内存队列
       * 定时任务:
       * 每分钟执行一次;执行15次后,若内存队列无内容调整调整为半小时执行一次
       */
    }
    System.out.println(res);
    return res;
  }


  /**
   * 拉取服务注册表
   * @return
   */
  public Map<String, Map<String, ServiceInstance>> fetchServiceRegistry() {
    Map<String, Map<String, ServiceInstance>> registry =
            new HashMap<String, Map<String, ServiceInstance>>();

    ServiceInstance serviceInstance = new ServiceInstance();
    serviceInstance.setHostname("finance-service-01");
    serviceInstance.setIp("192.168.31.1207");
    serviceInstance.setPort(9000);
    serviceInstance.setServiceInstanceId("FINANCE-SERVICE-192.168.31.207:9000");
    serviceInstance.setServiceName("FINANCE-SERVICE");

    Map<String, ServiceInstance> serviceInstances = new HashMap<String, ServiceInstance>();
    serviceInstances.put("FINANCE-SERVICE-192.168.31.207:9000", serviceInstance);

    registry.put("FINANCE-SERVICE", serviceInstances);

    System.out.println("拉取注册表：" + registry);

    return registry;
  }
}
