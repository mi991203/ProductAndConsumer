package springboot.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 消费者从redis中读取pipeline右侧数据，每次获取之前判断pipeline中是否还存在对象。
 * 多个线程同时读取pipeline里面的数据，需要保证读pipeline中size和数据的原子性
 */
@RestController
public class ConsumerController {
    @Autowired
    @Qualifier("thread-pool-product")
    private TaskExecutor taskExecutor;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private final Thread thread = new Thread(() -> {
        IntStream.range(0, 3).parallel().forEach(e -> {
            try {
                taskExecutor.execute(() -> {
                    // 三个消费线程
                    final Object person = redisTemplate.opsForList().rightPop("pipeline", 1, TimeUnit.SECONDS);
                    if (person != null) {
                        System.out.println(Thread.currentThread().getName() + "读取Redis中数据为：" + JSON.toJSONString(person));
                    } else {
                        System.out.println("pipeline中无可用对象");
                    }
                });
            } catch (Exception ex) {
                System.out.println("任务被拒绝");
                ex.printStackTrace();
            }
        });
    });

    @GetMapping("start-consume")
    public String startConsume() {
        // 延时0毫秒，每50毫秒间隔执行一次
        scheduledExecutorService.scheduleWithFixedDelay(thread, 0, 300, TimeUnit.MILLISECONDS);
        return "开启成功";
    }

    @GetMapping("stop-consume")
    public String stopConsume() {
        scheduledExecutorService.shutdown();
        return "消费任务终止";
    }
}
