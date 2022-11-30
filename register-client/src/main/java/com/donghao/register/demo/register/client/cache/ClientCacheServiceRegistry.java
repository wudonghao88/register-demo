package com.donghao.register.demo.register.client.cache;

/**
 * 服务器注册中心-缓存
 */
public class ClientCacheServiceRegistry {
  private Daemon daemon;

  public ClientCacheServiceRegistry() {
    daemon = new Daemon();
  }

  private class Daemon extends Thread {
    @Override
    public void run() {
    }
  }
}
