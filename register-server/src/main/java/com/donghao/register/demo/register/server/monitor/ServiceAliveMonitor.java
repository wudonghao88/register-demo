package com.donghao.register.demo.register.server.monitor;

import java.util.Map;

import com.donghao.register.demo.register.server.pojo.Registry;
import com.donghao.register.demo.register.server.pojo.ServiceInstance;

/**
 * 微服务存货状态监控后台线程
 *
 * @author donghao.wu
 */
public class ServiceAliveMonitor {
  /**
   * 服务实例过期时间
   */
  private static final Long TTL = 90 * 1000L;

  private Daemon daemon;

  private volatile boolean isRunning;

  public ServiceAliveMonitor() {
    this.daemon = new Daemon();
    /*
     * 设置了这个标志位，就代表这个线程是一个deamon线程，后台线程
     * 非deamon线程，我们一般叫做工作线程
     * 如果工作线程（main线程）结束了，deamon线程不会阻止jvm进程退出
     * deamon线程会跟jvm一起关闭
     */
    daemon.setDaemon(true);
    isRunning = true;
  }

  public void shutdown() {
    isRunning = false;
    daemon.interrupt();
  }

  /**
   * 启动后台线程
   */
  public void start() {
    daemon.start();
  }

  /**
   * 负责监控微服务存货状态的后台线程
   */
  private class Daemon extends Thread {
    /**
     * 注册表
     */
    private Registry registry = Registry.getInstance();

    /**
     * 如果某个服务90秒还没有更新心跳
     * 就认为他死了
     */
    @Override
    public void run() {
      System.out.println(Thread.currentThread().getName() + "线程的线程组是：" + Thread.currentThread().getThreadGroup());

      Map<String, Map<String, ServiceInstance>> registryMap;
      while (isRunning) {
        try {
          registryMap = registry.getRegistry();
          if (registryMap == null || registryMap.size() == 0) {
            Thread.sleep(60 * 1000L);
            continue;
          }
          for (String serviceName : registryMap.keySet()) {
            Map<String, ServiceInstance> serviceInstanceMap = registryMap.get(serviceName);
            if (serviceInstanceMap == null || serviceInstanceMap.size() <= 0) {
              System.out.println("注册表内存在服务名称：" + serviceName + "对应map 却为空的数据。");
              continue;
            }
            // 监控数据
            for (ServiceInstance serviceInstance : serviceInstanceMap.values()) {
              // 校验服务实例心跳续约时间是否长于过期时间
              if (serviceInstance.verifyLoseEfficacy(TTL)) {
                // 长于过期时间则 认为服务已经宕机 从注册表中删除该服务实例
                registry.remove(serviceInstance);
              }
            }
          }
          Thread.sleep(60 * 1000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
