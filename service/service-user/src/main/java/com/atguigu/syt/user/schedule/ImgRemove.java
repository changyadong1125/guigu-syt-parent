package com.atguigu.syt.user.schedule;

import com.atguigu.syt.model.user.UserInfo;
import com.atguigu.syt.rabbit.config.MQConst;
import com.atguigu.syt.user.mapper.UserInfoMapper;
import com.atguigu.syt.vo.ImgVo.IMGVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * project:guigu-syt-parent
 * package:com.atguigu.syt.user.schedule
 * class:ImgRemove
 *
 * @author: smile
 * @create: 2023/6/19-22:16
 * @Version: v1.0
 * @Description:
 */
@Component
@EnableScheduling
public class ImgRemove {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * return:
     * author: smile
     * version: 1.0
     * description:定时清理oss中的垃圾图片
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void ImgRemovePicture() {
        List<UserInfo> userInfos = userInfoMapper.selectList(null);
        userInfos.forEach(U -> {
            Long uId = U.getId();
            Set<Object> differenceSet = redisTemplate.opsForSet().difference(uId + ":beforeCommit", uId + ":afterCommit");
            System.out.println("======================>>>>>>>"+differenceSet);
            if (differenceSet != null) {
                IMGVo imgVo = new IMGVo();
                imgVo.setUid(uId);
                imgVo.setObjectsName(differenceSet);
                rabbitTemplate.convertAndSend(MQConst.EXCHANGE_DIRECT_USER_IMG, MQConst.ROUTING_KEY_USER_IMG, imgVo);
            }
        });
    }
}
