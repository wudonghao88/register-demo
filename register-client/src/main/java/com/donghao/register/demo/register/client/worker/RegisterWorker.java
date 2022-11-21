package com.donghao.register.demo.register.client.worker;

import com.donghao.register.demo.register.client.tools.HttpSender;
import com.donghao.register.demo.register.client.constants.ResponseCodeConstants;
import com.donghao.register.demo.register.client.pojo.HeartbeatRequest;
import com.donghao.register.demo.register.client.pojo.RegisterRequest;
import com.donghao.register.demo.register.client.pojo.RegisterResponse;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

/**
 * 负责想register-server发送注册的线程
 *
 * @author donghao.wu
 */
public class RegisterWorker extends Thread {
    public static final String SERVICE_NAME = "inventory-service";
    public static final String IP = "127.0.0.1";
    public static final String HOSTNAME = "inventory01";
    public static final int PORT = 9000;
    /**
     * http通讯组件
     */
    private HttpSender httpSender;
    private static final Logger logger = LoggerFactory.getLogger(RegisterWorker.class);
    /**
     * 是否完成服务注册
     */
    private Boolean finishedRegister;
    /**
     * 服务器实例id
     */
    private String serviceInstanceId;

    /**
     * Allocates a new {@code Thread} object. This constructor has the same
     * effect as {@linkplain #(ThreadGroup, Runnable, String) Thread}
     * {@code (null, null, gname)}, where {@code gname} is a newly generated
     * name. Automatically generated names are of the form
     * {@code "Thread-"+}<i>n</i>, where <i>n</i> is an integer.
     */
    public RegisterWorker(String serviceInstanceId) {
        httpSender = new HttpSender();
        finishedRegister = false;
        this.serviceInstanceId = serviceInstanceId;
    }

    @Override
    public void run() {
        if (!finishedRegister) {

        /*
        1.获取当前机器信息
        2.获取当前机器的IP地址,hostname,以及配置服务监听的服务端口号
        从配置文件中获取
        todo 参数放到yml里
         */
            RegisterRequest req = new RegisterRequest();
            req.setServiceName(SERVICE_NAME);
            req.setHostName(HOSTNAME);
            req.setIp(IP);
            req.setPort(PORT);
            req.setServiceInstanceId(serviceInstanceId);
            logger.debug(this.getClass().getName() + ".实例id:" + serviceInstanceId + ".开始注册");
            // 注册
            RegisterResponse register = httpSender.register(req);
            logger.debug(this.getClass().getName() + "服务注册结果" + register.toString());
            if (ResponseCodeConstants.SUCCESS.equals(register.getCode())) {
                finishedRegister = true;
            } else {
                return;
            }
            if (finishedRegister) {
                HeartbeatRequest heartbeat = new HeartbeatRequest();
                heartbeat.setServiceInstanceId(serviceInstanceId);
                while (true) {
                    // 不停地发送心跳
                    try {
                        //每隔30秒执行一次心跳
                        Thread.sleep(30 * 1000L);
                        RegisterResponse heatbeatRessponse = httpSender.heatbeat(heartbeat);
                        //
                        if (!ResponseCodeConstants.SUCCESS.equals(heatbeatRessponse.getCode())) {
                            logger.warn(this.getClass().getName() + ":心跳请求报错.内容为:" + heatbeatRessponse.toString());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
