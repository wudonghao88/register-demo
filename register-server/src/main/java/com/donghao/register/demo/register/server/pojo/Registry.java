package com.donghao.register.demo.register.server.pojo;

import java.util.HashMap;
import java.util.Map;

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
    public void register(ServiceInstance serviceInstance) {
        Map<String, ServiceInstance> serviceInstanceMap = registry.computeIfAbsent(serviceInstance.getServiceName(), k -> new HashMap<>());
        serviceInstanceMap.put(serviceInstance.getServiceInstanceId(), serviceInstance);
        System.out.println("服务实例：" + serviceInstance.toString() + " 完成注册");
        System.out.println("注册表：" + registry.toString());
    }

    /**
     * 根据服务名称和实例id获取 实例信息
     * @param serviceName 服务名称
     * @param serviceInstanceId 实例id
     * @return 实例信息
     */
    public ServiceInstance getServiceInstance(String serviceName,String serviceInstanceId){
        Map<String, ServiceInstance> serviceInstanceMap = registry.get(serviceName);
        if (serviceInstanceMap == null){
            System.out.println("心跳续约失败！服务名称为："+serviceName + "的服务没有注册到注册中心。");
            return null;
        }
        ServiceInstance serviceInstance = serviceInstanceMap.get(serviceInstanceId);
        if (serviceInstance  ==null){
            System.out.println("心跳续约失败！服务名称为："+serviceName + "且实例为："+serviceInstanceId+"服务没有注册到注册中心。");
            return null;
        }
        return serviceInstanceMap.get(serviceInstance);

    }
}
