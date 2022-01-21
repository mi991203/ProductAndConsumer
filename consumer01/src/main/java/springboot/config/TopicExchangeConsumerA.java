package springboot.config;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RabbitListener(queuesToDeclare = @Queue(RabbitMQConfig.TOPIC_EXCHANGE_QUEUE_A))
public class TopicExchangeConsumerA {
    @RabbitHandler
    public void process(Map<String, Object> map) {
        System.out.println("队列topic.queue.a开始消费：" + map.toString());
    }
}
