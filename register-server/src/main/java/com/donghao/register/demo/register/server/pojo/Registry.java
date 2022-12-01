package com.donghao.register.demo.register.server.pojo;

import java.util.HashMap;
import java.util.Map;

import com.donghao.register.demo.register.common.pojo.ServiceInstance;

/**
 * 注册表
 *
 * @author donghao.wu
 */
public class Registry {
  /**
   * 单例模式
   */
  private static Registry instance = new Registry();

  public Registry() {
  }

  /**
   * key:服务名称，value:key:实例id，实例信息
   */
  private Map<String, Map<String, ServiceInstance>> registry = new HashMap<>();

  public static Registry getInstance() {
    return instance;
  }

  /**
   *
   */
  public synchronized void register(ServiceInstance serviceInstance) {
    Map<String, ServiceInstance> serviceInstanceMap = registry.computeIfAbsent(serviceInstance.getServiceName(), k -> new HashMap<>());
    serviceInstanceMap.put(serviceInstance.getServiceInstanceId(), serviceInstance);
    System.out.println("服务实例：" + serviceInstance.toString() + " 完成注册");
    System.out.println("注册表：" + registry.toString());
  }

  /**
   * 根据服务名称和实例id获取 实例信息
   *
   * @param serviceName 服务名称
   * @param serviceInstanceId 实例id
   * @return 实例信息
   */
  public synchronized ServiceInstance getServiceInstance(String serviceName, String serviceInstanceId) {
    Map<String, ServiceInstance> serviceInstanceMap = registry.get(serviceName);
    if (serviceInstanceMap == null) {
      System.out.println("心跳续约失败！服务名称为：" + serviceName + "的服务没有注册到注册中心。");
      return null;
    }
    ServiceInstance serviceInstance = serviceInstanceMap.get(serviceInstanceId);
    if (serviceInstance == null) {
      System.out.println("心跳续约失败！服务名称为：" + serviceName + "且实例为：" + serviceInstanceId + "服务没有注册到注册中心。");
      return null;
    }
    return serviceInstanceMap.get(serviceInstance);

  }

  /**
   * 获取注册表信息
   *
   * @return 注册表信息
   */
  public synchronized Map<String, Map<String, ServiceInstance>> getRegistry() {
    return registry;
  }

  /**
   * 清除已经宕机的服务器实例
   *
   * @param serviceName serviceName
   * @param serviceInstanceId serviceInstanceId
   */
  public synchronized void remove(String serviceName, String serviceInstanceId) {
    if (serviceName == null || serviceInstanceId == null) {
      return;
    }
    Map<String, ServiceInstance> serviceInstanceMap = registry.get(serviceName);

    if (serviceInstanceMap == null || serviceInstanceMap.size() == 0) {
      return;
    }
    serviceInstanceMap.remove(serviceInstanceId);
    if (serviceInstanceMap == null || serviceInstanceMap.size() <= 0) {
      registry.remove(serviceName);
      if (registry == null || registry.size() < 0) {
        registry = new HashMap<>();
      }
    }
    System.out.println(
        this.getClass().getName() + ".remove:服务名称为" + serviceName + "下的实例：" + serviceInstanceId + "已宕机，现在成功从注册表中删除.现在服务器注册表内容为：" + registry.toString());
  }
}
