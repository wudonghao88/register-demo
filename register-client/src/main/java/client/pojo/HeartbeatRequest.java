package client.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 心跳请求
 *
 * @author donghao.wu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class HeartbeatRequest {
    /**
     * 服务实例id
     */
    private String serviceInstanceId;
}
