package com.donghao.register.demo.register.server.tools;

/**
 * 心跳计数器
 * todo 数不准确
 */
public class HeartbeatMessuredRate {
  private static HeartbeatMessuredRate instance = new HeartbeatMessuredRate();

  private HeartbeatMessuredRate() {
    Deamon deamon = new Deamon();
    // 设置后台线程 主线程关闭后后台线程自动关闭
    deamon.setDaemon(true);
    deamon.start();
  }

  /**
   * 最近一分钟的心跳次数
   */
  private Long latestMinuteHeartbeatRate = 0L;

  /**
   * 最近一分钟的时间戳
   */
  private Long latestMinuteTimestamp = System.currentTimeMillis();

  public static HeartbeatMessuredRate getInstance() {
    return instance;
  }

  public void increment() {
    synchronized (HeartbeatMessuredRate.class) {
      latestMinuteHeartbeatRate++;
    }
  }

  public synchronized Long get() {
    return latestMinuteHeartbeatRate;
  }

  private class Deamon extends Thread {
    @Override
    public void run() {
      while (true) {
        if (System.currentTimeMillis() - latestMinuteTimestamp > 60 * 1000L) {
          synchronized (HeartbeatMessuredRate.class) {
            latestMinuteHeartbeatRate = 0L;
            latestMinuteTimestamp = System.currentTimeMillis();
          }
          try {
            Thread.sleep(59 * 1000L);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }
}
