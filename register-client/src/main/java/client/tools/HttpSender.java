package client.tools;

import com.alibaba.fastjson.JSONObject;
import com.donghao.register.demo.register.client.constants.ResponseStatusConstants;
import com.donghao.register.demo.register.client.pojo.HeartbeatRequest;
import com.donghao.register.demo.register.client.pojo.RegisterRequest;
import com.donghao.register.demo.register.client.pojo.RegisterResponse;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

import static com.donghao.register.demo.register.client.constants.ResponseCodeConstants.FAILURE_TWO;

/**
 * 负责发送http请求的组件
 *
 * @author donghao.wu
 */
public class HttpSender {
    private static final String URL = "";
    private static final Logger logger = LoggerFactory.getLogger(HttpSender.class);

    public RegisterResponse register(RegisterRequest req) {
        /*
        构造请求,放入示例名称,服务名称,ip地址
        端口号等
         */
        logger.debug(this.getClass().getSimpleName() + "服务实例发送请求进行注册");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONObject.toJSONString(req));
        Request request = new Request.Builder().url(URL).post(body).build();
        RegisterResponse res = new RegisterResponse();
        try {
            Response execute = client.newCall(request).execute();
            res.setStatus(execute.isSuccessful() ? ResponseStatusConstants.SUCCESS : ResponseStatusConstants.FAILURE);
            res.setCode(execute.code());
        } catch (IOException e) {
            e.printStackTrace();
            //直接打message
            logger.warn(e.getMessage());
            res.setCode(FAILURE_TWO);
            res.setStatus(ResponseStatusConstants.FAILURE);
            res.setMessage(e.getMessage());
            //todo 可以增加重试逻辑
            /*
            重试逻辑设计
            异步
            内循环 3次 每秒重试一次 3次都失败则写入内存队列
            定时任务:
            每分钟执行一次;执行15次后,若内存队列无内容调整调整为半小时执行一次
             */
        }
        System.out.println(res);
        return res;
    }

    public RegisterResponse heatbeat(HeartbeatRequest req) {
        /*
        发送请求进行心跳
        构造请求,放入示例名称,服务名称,ip地址
        端口号等
         */
        logger.debug(this.getClass().getSimpleName() + "服务实例发送请求进行注册");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONObject.toJSONString(req));
        Request request = new Request.Builder().url(URL).post(body).build();
        RegisterResponse res = new RegisterResponse();
        try {
            logger.debug("发送请求");
            Response execute = client.newCall(request).execute();
            res.setStatus(execute.isSuccessful() ? ResponseStatusConstants.SUCCESS : ResponseStatusConstants.FAILURE);
            res.setCode(execute.code());
        } catch (IOException e) {
            e.printStackTrace();
            //直接打message
            logger.warn(e.getMessage());
            res.setCode(FAILURE_TWO);
            res.setStatus(ResponseStatusConstants.FAILURE);
            res.setMessage(e.getMessage());
            //todo 可以增加重试逻辑
            /*
            重试逻辑设计
            异步
            内循环 3次 每秒重试一次 3次都失败则写入内存队列
            定时任务:
            每分钟执行一次;执行15次后,若内存队列无内容调整调整为半小时执行一次
             */
        }
        System.out.println(res);
        return res;
    }
}
