package springboot.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.service.LockService;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class TaskController {
    private ExecutorService fixExecutorService = Executors.newFixedThreadPool(2);
    private static volatile boolean stopFlag = true;

    @Resource
    private LockService lockService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/start-consume")
    public String startConsume() {
        stopFlag = false;
        Runnable runnable = () -> {
            while (!stopFlag) {
                final String uuid = UUID.randomUUID().toString();
                while (!lockService.lock("lockKey", uuid)) {
                    try {
                        // 避免频繁申请锁
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 开启WatchDog
                lockService.renewalTime("lockKey", uuid, Duration.ofSeconds(5));
                try {
                    final Object person = redisTemplate.opsForList().rightPop("pipeline", 1, TimeUnit.SECONDS);
                    if (person != null) {
                        System.out.print(Thread.currentThread().getName() + "开始消费: " + JSON.toJSONString(person));
                        // 模拟处理耗时7s
                        Thread.sleep(7000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lockService.unlock("lockKey", uuid);
                }
            }
        };
        final Thread thread1 = new Thread(runnable, "工作线程1");
        final Thread thread2 = new Thread(runnable, "工作线程2");
        fixExecutorService.execute(thread1);
        fixExecutorService.execute(thread2);
        return "消费开启";
    }

    @GetMapping("/stop-consume")
    public String stopConsume() {
        stopFlag = true;
        return "消费停止";
    }

    @GetMapping("test")
    public Boolean getLock() {
        return lockService.lock("lockKey", "This is value");
    }


}
