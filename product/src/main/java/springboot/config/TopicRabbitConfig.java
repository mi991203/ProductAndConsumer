package springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class TopicRabbitConfig implements BeanPostProcessor {
    @Resource
    private RabbitAdmin rabbitAdmin;

    /*
     * 1、name:    队列名称
     * 2、durable: 是否持久化
     * 3、exclusive: 是否独享、排外的。如果设置为true，定义为排他队列。则只有创建者可以使用此队列。也就是private私有的。
     * 4、autoDelete: 是否自动删除。也就是临时队列。当最后一个消费者断开连接后，会自动删除。
     * */
    @Bean
    public Queue topicExchangeQueueA() {
        //队列A
        return new Queue(RabbitMQConfig.TOPIC_EXCHANGE_QUEUE_A, true, false, false);
    }

    @Bean
    public Queue topicExchangeQueueB() {
        //队列B
        return new Queue(RabbitMQConfig.TOPIC_EXCHANGE_QUEUE_B, true, false, false);
    }

    @Bean
    public Queue topicExchangeQueueC() {
        return new Queue(RabbitMQConfig.TOPIC_EXCHANGE_QUEUE_C, true, false, false);
    }

    @Bean
    public TopicExchange rabbitmqDemoTopicExchange() {
        //创建FanoutExchange类型交换机
        return new TopicExchange(RabbitMQConfig.TOPIC_EXCHANGE_DEMO_NAME, true, false);
    }

    @Bean
    public Binding bindFanoutA() {
        //队列A绑定到FanoutExchange交换机
        return BindingBuilder.bind(topicExchangeQueueA()).to(rabbitmqDemoTopicExchange()).with("topic.#");
    }

    @Bean
    public Binding bindFanoutB() {
        //队列B绑定到FanoutExchange交换机
        return BindingBuilder.bind(topicExchangeQueueB()).to(rabbitmqDemoTopicExchange()).with("a.*");
    }

    @Bean
    public Binding bindTopicC() {
        return BindingBuilder.bind(topicExchangeQueueC()).to(rabbitmqDemoTopicExchange()).with("a.*");
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        final RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //启动项目即创建交换机和队列
        rabbitAdmin.declareExchange(rabbitmqDemoTopicExchange());
        rabbitAdmin.declareQueue(topicExchangeQueueA());
        rabbitAdmin.declareQueue(topicExchangeQueueB());
        rabbitAdmin.declareQueue(topicExchangeQueueC());
        return null;
    }
}
