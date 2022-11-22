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
        Map<String, ServiceInstance> ServiceInstanceMap = registry.get(serviceInstance.getServiceName());
        if (ServiceInstanceMap == null) {
            ServiceInstanceMap = new HashMap<>();
            registry.put(serviceInstance.getServiceName(), ServiceInstanceMap);
        }
        ServiceInstanceMap.put(serviceInstance.getServiceInstanceId(), serviceInstance);
        System.out.println("服务实例：" + serviceInstance.toString() + " 完成注册");
        System.out.println("注册表：" + registry.toString());
    }
}
