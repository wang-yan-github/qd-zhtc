package com.jsdc.zhtc.task;

import com.jsdc.zhtc.payPolymerize.Token;
import com.jsdc.zhtc.payPolymerize.TokenUtil;
import com.jsdc.zhtc.payPolymerize.UMSConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 * 更新token保存到redis
 * 每10秒执行一次
 */
@Component
public class tokenSchedule {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TokenUtil tokenUtil;

    /**
     * 更新token保存到redis
     * 每10秒执行一次
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void updateToken() {
        long expireTime = 3600;
        long advanceTime = 600;
        //redis中是否存在accessToken
        if (redisTemplate.hasKey(UMSConfig.REDIS_TOKEN)) {
            //查询accessToken过期时间
            Long expired = redisTemplate.getExpire(UMSConfig.REDIS_TOKEN, TimeUnit.SECONDS);

            //提前十分钟重新获取token
            if (expired < advanceTime) {
                Token token = tokenUtil.getToken();
                redisTemplate.opsForValue().set(UMSConfig.REDIS_TOKEN, token.getAccessToken(), expireTime, TimeUnit.SECONDS);
            }
            System.out.println(expired);
        } else {
            //如果没有accessToken，新生成并存入redis
            Token token = tokenUtil.getToken();
            redisTemplate.opsForValue().set(UMSConfig.REDIS_TOKEN, token.getAccessToken(), expireTime, TimeUnit.SECONDS);
        }
    }
}
