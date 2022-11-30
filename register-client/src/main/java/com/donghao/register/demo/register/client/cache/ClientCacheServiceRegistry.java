package com.donghao.register.demo.register.client.cache;

import java.util.HashMap;
import java.util.Map;

import com.donghao.register.demo.register.client.RegisterClient;
import com.donghao.register.demo.register.client.tools.HttpSender;
import com.donghao.register.demo.register.common.pojo.ServiceInstance;

/**
 * 服务器注册中心-缓存
 */
public class ClientCacheServiceRegistry {
  /**
   * 服务注册表数据拉取间隔
   */
  private static final Long SERVICE_REGISTRY_FETCH_INTERVAL = 30 * 1000L;

  /**
   * 定时拉取注册表信息
   */
  private Daemon daemon;

  private HttpSender httpSender;

  /**
   * 定时拉取注册表信息
   */
  private RegisterClient registerClient;

  /**
   * 注册表信息
   */
  private volatile Map<String, Map<String, ServiceInstance>> registry = new HashMap<>();

  public ClientCacheServiceRegistry(RegisterClient registerClient, HttpSender httpSender) {
    this.daemon = new Daemon();
    this.registerClient = registerClient;
    this.httpSender = httpSender;
  }

  public void initialize() {
    this.daemon.start();
  }

  public void destroy() {
    this.daemon.interrupt();
  }

  /**
   * 获取服务注册表
   * 
   * @return 获取服务注册表
   */
  public Map<String, Map<String, ServiceInstance>> getRegistry() {
    return registry;
  }

  private class Daemon extends Thread {
    @Override
    public void run() {
      while (registerClient.isRunning()) {
        // 从registry-server同步数据

        try {
          registry = httpSender.fetchServiceRegistry();
          //
          Thread.sleep(SERVICE_REGISTRY_FETCH_INTERVAL);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    }
  }
}
