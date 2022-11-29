package com.donghao.register.demo.register.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 代表了一个服务实例
 * 包含了一个服务实例的所有信息。
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class ServiceInstance {
  /**
   * 服务名称
   */
  private String serviceName;

  /**
   * ip地址
   */
  private String ip;

  /**
   * 服务名称
   */
  private String hostName;

  /**
   * 端口号
   */
  private int port;

  /**
   * 服务名称
   */
  private String serviceInstanceId;

  /**
   * 契约
   */
  private Lease lease;

  public ServiceInstance() {
    this.lease = new Lease();
  }

  /**
   * 续约心跳时间
   */
  public void renew() {
    lease.renew();
  }

  /**
   * 校验服务实例心跳续约时间是否长于过期时间
   *
   * @param ttl 过期时间
   * @return 是否过期
   */
  public boolean verifyLoseEfficacy(Long ttl) {
    if ((System.currentTimeMillis() - lease.getLatestHeartbeatTime()) > ttl) {
      System.out.println("服务实例不在存活：服务名称-" + this.serviceName + "\t实例-" + this.serviceInstanceId);
      return false;
    }
    System.out.println("服务实例存活：服务名称-" + this.serviceName + "\t实例-" + this.serviceInstanceId);
    return true;
  }

  /**
   * 契约
   * 维护了服务实例和当前的注册中心服务的联系
   *
   * @author donghao.wu
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private class Lease {
    /**
     * 最近一次心跳的时间
     */
    private volatile Long latestHeartbeatTime = System.currentTimeMillis();

    public void renew() {
      this.latestHeartbeatTime = System.currentTimeMillis();
      System.out.println("服务实例：" + serviceInstanceId + "续约成功！续约时间为：" + latestHeartbeatTime);
    }
  }
}
