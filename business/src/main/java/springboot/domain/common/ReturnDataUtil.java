package springboot.domain.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ReturnDataUtil {
    public static final String SUCCESS = "0";
    public static final String FAILED = "1";

    private ReturnDataUtil() {

    }

    /**
     * 返回字符串数据
     */
    public static ReturnDataBean returnString(String data) {
        ReturnDataBean returnBean = new ReturnDataBean();
        returnBean.setCode(ReturnDataUtil.SUCCESS);
        returnBean.setMessage("SUCCESS");
        Map<String, String> map = new HashMap<String, String>();
        map.put("value", data);
        returnBean.setData(map);//data直接放字符串报错
        return returnBean;
    }

    /**
     * 返回Res对象
     */
    public static <T> ReturnDataBean returnObject(T data) {
        ReturnDataBean returnBean = new ReturnDataBean();
        returnBean.setCode(ReturnDataUtil.SUCCESS);
        returnBean.setMessage("SUCCESS");
        returnBean.setData(data);
        return returnBean;
    }

    /**
     * 返回list
     */
    public static <T> ReturnDataBean returnList(List<T> data) {
        ReturnDataBean returnBean = new ReturnDataBean();
        returnBean.setCode(ReturnDataUtil.SUCCESS);
        returnBean.setMessage("SUCCESS");
        returnBean.setData(data);
        return returnBean;
    }

    /**
     * 返回List和记录数
     */
    public static <T> ReturnDataBean returnList(List<T> data, long count) {
        ReturnDataBean returnBean = new ReturnDataBean();
        returnBean.setCode(ReturnDataUtil.SUCCESS);
        returnBean.setMessage("SUCCESS");
        PackListBean packListBean = new PackListBean();
        packListBean.setCount(count);
        packListBean.setList(data);
        returnBean.setData(packListBean);
        return returnBean;
    }

    /**
     * 返回错误信息
     */
    public static ReturnDataBean returnError(String errmsg) {
        ReturnDataBean returnBean = new ReturnDataBean();
        returnBean.setCode(ReturnDataUtil.FAILED);
        returnBean.setMessage(errmsg);
        return returnBean;
    }

    /**
     * 返回错误编码和错误信息
     */
    public static ReturnDataBean returnError(String errcode, String errmsg) {
        ReturnDataBean returnBean = new ReturnDataBean();
        returnBean.setCode(errcode);
        returnBean.setMessage(errmsg);
        return returnBean;
    }


}
