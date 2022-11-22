package com.donghao.register.demo.register.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 契约
 * 维护了服务实例和当前的注册中心服务的联系
 *
 * @author donghao.wu
 */
@Data
@AllArgsConstructor
public class Lease {
    /**
     * 最近一次心跳的时间
     */
    private Long latestHeartbeatTime= 0L;

    public Lease() {
        this.latestHeartbeatTime = System.currentTimeMillis();
    }
    public void reNew(){
        this.latestHeartbeatTime = System.currentTimeMillis();
    }
}
