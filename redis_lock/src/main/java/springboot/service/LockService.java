package springboot.service;


import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class LockService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public Boolean lock(String lockKey, String requestId) {
        return redisTemplate.execute((RedisCallback<Boolean>) (connection) -> connection.set(lockKey.getBytes(StandardCharsets.UTF_8),
                requestId.getBytes(StandardCharsets.UTF_8), Expiration.seconds(5), RedisStringCommands.SetOption.ifAbsent()));
    }

    public void unlock(String lockKey, String requestId) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setResultType(Long.class);
        script.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end");
        redisTemplate.execute(script, Collections.singletonList(lockKey), requestId);
    }

    public void renewalTime(String lockKey, String requestId, Duration duration) {
        final long halfDuration = duration.getSeconds() / 2;
        new Thread(() -> {
            // 判断当前lockKey的值对应的是不是requestId，如果是，那么续约。
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setResultType(Long.class);
            script.setScriptText("if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('set', KEYS[1], ARGV[1], ARGV[2], " +
                    "ARGV[3], ARGV[4]) else return 0 end");
            final List<String> argvList = new ArrayList<>();
            argvList.add(requestId);
            argvList.add("ex");
            argvList.add(duration.getSeconds() + "");
            argvList.add("xx");
            while (true) {
                final Long executeStr = redisTemplate.execute(script, Collections.singletonList(lockKey), argvList);
                if (executeStr != null && 0 == executeStr) {
                    break;
                }
                try {
                    Thread.sleep(halfDuration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
