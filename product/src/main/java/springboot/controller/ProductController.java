package springboot.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.domain.bean.Person;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
public class ProductController {
    @Autowired
    @Qualifier("thread-pool-product")
    private TaskExecutor executor;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 开启一个线程，这个线程工作时周期性的产生Person对象，并传入异步线程池给线程处理——将产生的对象序列化存到redis
    private ScheduledExecutorService scheduledExecutorService;

    // 工作线程
    final Thread thread = new Thread(() -> {
        // 每隔5s产生100个对象
        final List<Person> taskList = IntStream.range(0, 100).mapToObj(i -> new Person("Jack-" + i, "Jack-" + i + "的个人介绍", i))
                .collect(Collectors.toList());
        distributeTask(taskList);
    });

    @GetMapping("start-product")
    public String startProduct() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newScheduledThreadPool(1);
        }
        scheduledExecutorService.scheduleAtFixedRate(thread, 0, 5, TimeUnit.SECONDS);
        return "任务开启";
    }

    private void distributeTask(final List<Person> taskList) {
        // 8个线程处理
        final int totalSize = taskList.size();
        // 拆分任务
        int perTaskCount = totalSize / 8;
        for (int i = 0; i < 8; i++) {
            if (i != 7) {
                executor.execute(new ProductRun(i * perTaskCount, (i + 1) * perTaskCount, taskList));
            } else {
                executor.execute(new ProductRun(i * perTaskCount, totalSize, taskList));
            }
        }
    }

    class ProductRun implements Runnable {
        private final int start;
        private final int end;
        private final List<Person> taskList;

        public ProductRun(int start, int end, List<Person> taskList) {
            this.start = start;
            this.end = end;
            this.taskList = taskList;
        }

        @Override
        public void run() {
            try {
                // 将对象塞入redis的队列里
                System.out.println(Thread.currentThread().getName() + "正在工作");
                for (int i = start; i < end; i++) {
                    redisTemplate.opsForList().leftPush("pipeline", taskList.get(i));
                }
                // 模拟任务耗时
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("stop-product")
    public String stopProduct() {
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
        scheduledExecutorService = null;
        return "停止成功";
    }

}
