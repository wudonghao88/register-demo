package com.donghao.register.demo.register.server.pojo;

/**
 * 契约
 * 维护了服务实例和当前的注册中心服务的联系
 *
 * @author donghao.wu
 */
public class Lease {
    /**
     * 最近一次心跳的时间
     */
    private Long latestHeartbeatTime= 0L;
}
