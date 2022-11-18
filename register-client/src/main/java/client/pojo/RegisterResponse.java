package client.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 注册相应
 *
 * @author donghao.wu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RegisterResponse {
    /**
     * 响应码:100,200,300
     */
    private Integer code;
    /**
     * x响应状态
     * success,failure
     */
    private String status;
    /**
     * message
     */
    private String message;

    @Override
    public String toString() {
        return "RegisterResponse{" +
                "code=" + code +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
