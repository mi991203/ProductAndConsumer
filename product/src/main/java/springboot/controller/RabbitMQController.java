package springboot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.RabbitMQService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/mall/rabbitmq")
public class RabbitMQController {
    @Resource
    private RabbitMQService rabbitMQService;
    /**
     * 发送消息
     * @author java技术爱好者
     */
    @PostMapping("/sendDirectMsg")
    public String sendDirectMsg(@RequestParam(name = "msg") String msg) {
        return rabbitMQService.sendDirectMsg(msg);
    }

    @PostMapping("sendFanoutMsg")
    public String sendFanoutMsg(@RequestParam(name = "msg") String msg) {
        return rabbitMQService.sendFanoutMsg(msg);
    }

    @PostMapping("sendTopicMsg")
    public String sendTopicMsg(@RequestParam(name = "msg") String msg, @RequestParam("routingKey") String routingKey) {
        return rabbitMQService.sendTopicMsg(msg, routingKey);
    }


}
