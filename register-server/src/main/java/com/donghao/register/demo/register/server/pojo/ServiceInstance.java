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
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "ServiceInstance{" +
                "serviceName='" + serviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", hostName='" + hostName + '\'' +
                ", port=" + port +
                ", serviceInstanceId='" + serviceInstanceId + '\'' +
                ", lease=" + lease +
                '}';
    }
}
