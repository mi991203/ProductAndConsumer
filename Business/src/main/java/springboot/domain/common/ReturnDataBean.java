package springboot.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ReturnDataBean {
    /**错误码*/
    @JsonProperty("error_no")
    private Integer errorNo;
    /**错误信息提示*/
    @JsonProperty("error_info")
    private String errorInfo;
    /**返回的数据*/
    @JsonProperty("data")
    private Object data;

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReturnDataBean{");
        sb.append("error_no='").append(errorNo).append('\'');
        sb.append(", error_info='").append(errorInfo).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

    public void setCode(String code){
        this.errorNo = Integer.valueOf(code);
    }

    public void setMessage(String message){
        this.errorInfo = message;
    }
}
