package com.donghao.register.demo.register.client;

import java.util.UUID;

import com.donghao.register.demo.register.client.cache.ClientCacheServiceRegistry;
import com.donghao.register.demo.register.client.tools.HttpSender;
import com.donghao.register.demo.register.common.constants.ResponseCodeConstants;
import com.donghao.register.demo.register.common.pojo.HeartbeatRequest;
import com.donghao.register.demo.register.common.pojo.RegisterRequest;
import com.donghao.register.demo.register.common.pojo.RegisterResponse;

/**
 * 在服务上被创建和启动,负责和register-server进行通信
 *
 * @author donghao.wu
 */
public class RegisterClient {

  private String serviceInstanceId;

  private RegisterWorker registerWorker;

  private HeartbeatWorker heartbeatWorker;

  private ClientCacheServiceRegistry registry;

  public static final String SERVICE_NAME = "inventory-service";

  public static final String IP = "127.0.0.1";

  public static final String HOSTNAME = "inventory01";

  public static final int PORT = 9000;

  /**
   * 是否完成服务注册
   */
  private Boolean finishedRegister;

  /**
   * http通讯组件
   */
  private HttpSender httpSender;

  /**
   * http通讯组件
   */
  private volatile boolean isRunning;

  public RegisterClient() {
    httpSender = new HttpSender();
    finishedRegister = false;
    isRunning = true;
    this.serviceInstanceId = UUID.randomUUID().toString().replace("-", "");
    this.registerWorker = new RegisterWorker();
    this.heartbeatWorker = new HeartbeatWorker();
    this.registry = new ClientCacheServiceRegistry(this, httpSender);
  }

  public void start() {
    try {
      /*
       * 一旦启动组件之后:
       * 1.想register-server发送请求,注册这个服务
       * 2.注册成功后,开启另一个进程发送心跳
       */
      this.registerWorker.start();
      this.registerWorker.join();
      this.heartbeatWorker.start();
      // 初始化注册表缓存信息
      registry.initialize();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void shutdown() {
    isRunning = false;
    this.heartbeatWorker.interrupt();
    this.registry.destroy();
    this.httpSender.cancel(SERVICE_NAME, serviceInstanceId);
  }

  /**
   * 负责向register-server发送注册的线程
   *
   * @author donghao.wu
   */
  private class RegisterWorker extends Thread {
    @Override
    public void run() {
      if (!finishedRegister) {
        /*
         * 1.获取当前机器信息
         * 2.获取当前机器的IP地址,hostname,以及配置服务监听的服务端口号
         * 从配置文件中获取
         * todo 参数放到yml里
         */
        RegisterRequest req = new RegisterRequest();
        req.setServiceName(SERVICE_NAME);
        req.setHostName(HOSTNAME);
        req.setIp(IP);
        req.setPort(PORT);
        req.setServiceInstanceId(serviceInstanceId);
        System.out.println(this.getClass().getName() + ".实例id:" + serviceInstanceId + ".开始注册");
        // 注册
        RegisterResponse register = httpSender.register(req);
        System.out.println(this.getClass().getName() + "服务注册结果" + register.toString());
        if (ResponseCodeConstants.SUCCESS.equals(register.getCode())) {
          finishedRegister = true;
        }
      }
    }
  }

  /**
   * 心跳线程
   *
   * @author donghao.wu
   */
  private class HeartbeatWorker extends Thread {

    @Override
    public void run() {
      if (finishedRegister) {
        HeartbeatRequest heartbeat = new HeartbeatRequest();
        heartbeat.setServiceInstanceId(serviceInstanceId);
        heartbeat.setServiceName(SERVICE_NAME);
        while (isRunning) {
          // 不停地发送心跳
          try {
            // 每隔30秒执行一次心跳
            Thread.sleep(30 * 1000L);
            RegisterResponse heatbeatRessponse = httpSender.heatbeat(heartbeat);
            //
            if (!ResponseCodeConstants.SUCCESS.equals(heatbeatRessponse.getCode())) {
              System.out.println(this.getClass().getName() + ":心跳请求报错.内容为:" + heatbeatRessponse.toString());
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * 返回registerClient是否正在运行
   * 
   * @return isRunning
   */
  public boolean isRunning() {
    return isRunning;
  }
}
