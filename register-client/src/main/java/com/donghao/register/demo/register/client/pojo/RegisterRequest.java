package com.donghao.register.demo.register.client.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 注册请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RegisterRequest {
    /**
     * 服务器名称
     */
    private String serviceName;
    /**
     * 服务所在的主机名
     */
    private String hostName;
    /**
     * 服务所在机器的ip地址
     */
    private String ip;
    /**
     * 服务所在机器的port
     */
    private int port;
    /**
     * 服务实例id
     */
    private String serviceInstanceId;
}
