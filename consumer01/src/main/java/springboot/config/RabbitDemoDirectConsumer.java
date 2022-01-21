package springboot.config;

import com.google.common.collect.MapMaker;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queuesToDeclare = @Queue(RabbitMQConfig.RABBITMQ_DEMO_TOPIC))
public class RabbitDemoDirectConsumer {
    @RabbitHandler
    public void process(Map<String, Object> map) {
        System.out.println("消费者消费rabbitmqDemoTopic队列: " + map.toString());
    }
}
