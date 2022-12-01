package com.donghao.register.demo.register.server.pojo;

import com.donghao.register.demo.register.server.tools.HeartbeatMessuredRate;

/**
 * 自我保护机制
 */
public class SelfprotectionPolicy {
  private static SelfprotectionPolicy instance = new SelfprotectionPolicy();

  /**
   * 每分钟 期望心跳次数
   * 如果有10个服务实例，这个数值就是10*2 = 20
   * 因为30秒一次心跳
   * todo 目前心跳频率为硬编码
   */
  private long expectedHeartbeatRate = 0L;

  /**
   * 每分钟 期望心跳次数阈值
   * 如果有10个服务实例，这个数值就是10*2*0.85 = 17
   * 每分钟至少17次心跳，才可以不用进入自我保护机制
   */
  private long expectedHeartbeatThreshold = 0L;

  /**
   * 自我保护机制是否启用
   * 
   * @return 是否
   */
  public Boolean isEnable() {
    HeartbeatMessuredRate heartbeatMessuredRate = HeartbeatMessuredRate.getInstance();
    // 最近一分钟
    Long latestMinuteHeartbeatRate = heartbeatMessuredRate.get();
    if (latestMinuteHeartbeatRate == null) {
      // 不可能走到
    }
    if (this.expectedHeartbeatThreshold == 0) {
      System.out.println("保护机制开启：没有服务注册进来。");
      return true;
    }
    if (latestMinuteHeartbeatRate < this.expectedHeartbeatThreshold) {
      System.out.println("保护机制开启：最近一分钟心跳次数：" + latestMinuteHeartbeatRate + "小于 每分钟 期望心跳次数阈值：" + this.expectedHeartbeatThreshold);
      return true;
    }
    System.out.println("保护机制未开启：最近一分钟心跳次数：" + latestMinuteHeartbeatRate + "小于 每分钟 期望心跳次数阈值：" + this.expectedHeartbeatThreshold);
    return false;
  }

  public static SelfprotectionPolicy getInstance() {
    return instance;
  }

  public static void setInstance(SelfprotectionPolicy instance) {
    SelfprotectionPolicy.instance = instance;
  }

  public long getExpectedHeartbeatRate() {
    return expectedHeartbeatRate;
  }

  public void setExpectedHeartbeatRate(long expectedHeartbeatRate) {
    this.expectedHeartbeatRate = expectedHeartbeatRate;
  }

  public long getExpectedHeartbeatThreshold() {
    return expectedHeartbeatThreshold;
  }

  public void setExpectedHeartbeatThreshold(long expectedHeartbeatThreshold) {
    this.expectedHeartbeatThreshold = expectedHeartbeatThreshold;
  }
}
