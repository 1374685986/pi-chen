package com.chen.service.impl;

import com.chen.entity.ClientInfo;
import com.chen.entity.vo.ClientRegisterInfo;
import com.chen.utils.RedisUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.chen.service.ClientService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private RedisUtil redisUtil;

    // 注册在线
    @Override
    public ClientRegisterInfo register() {
        String clientId = UUID.randomUUID().toString();
        redisUtil.sSetAndTime("client_pi_chen",5,clientId);
        ClientRegisterInfo clientRegisterInfo = new ClientRegisterInfo();
        clientRegisterInfo.setClientId(clientId);
        return clientRegisterInfo;
    }

    @Override
    public void heart(String clientId) {
        // 在缓存中判断客户端是否还在线，5分钟的过期时间
        redisUtil.sHasKey("client_pi_chen",clientId);
    }

    @Override
    public Long clientNum() {
        return redisUtil.sGetSetSize("client_pi_chen");
    }

    @Override
    public ClientInfo getClientInfo() {
        // 获取客户端的信息
        ClientInfo info = new ClientInfo();
        //  只能获取客户端数量？
        info.setClients(redisUtil.sGetSetSize("client_pi_chen"));
        return info;
    }
}
