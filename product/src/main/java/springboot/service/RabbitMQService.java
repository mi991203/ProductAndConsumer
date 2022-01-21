package springboot.service;

public interface RabbitMQService {
    String sendDirectMsg(String msg);

    String sendFanoutMsg(String msg);

    String sendTopicMsg(String msg, String routingKey);
}
