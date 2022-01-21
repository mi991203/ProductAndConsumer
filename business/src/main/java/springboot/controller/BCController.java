package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.common.ReturnDataBean;
import springboot.domain.common.ReturnDataUtil;
import springboot.remote.ConsumerGetPersonApi;

@RestController
public class BCController {
    @Autowired
    private ConsumerGetPersonApi consumerGetPersonApi;

    @GetMapping("get-one-person")
    public ReturnDataBean getOnePerson() {
        return ReturnDataUtil.returnObject(consumerGetPersonApi.getOnePerson());
    }
}
