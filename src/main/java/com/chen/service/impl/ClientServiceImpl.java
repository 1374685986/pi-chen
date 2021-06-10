package com.chen.service.impl;

import com.chen.entity.ClientInfo;
import com.chen.entity.vo.ClientRegisterInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;
import com.chen.service.ClientService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ClientServiceImpl implements ClientService {


    // build a cache to store clientInfo
    private final Cache<String,String> clients =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();


    // 注册在线
    @Override
    public ClientRegisterInfo register() {
        String clientId = UUID.randomUUID().toString();
        clients.put(clientId,clientId);
        ClientRegisterInfo clientRegisterInfo = new ClientRegisterInfo();
        clientRegisterInfo.setClientId(clientId);
        return clientRegisterInfo;
    }

    @Override
    public void heart(String clientId) {
        // 在缓存中判断客户端是否还在线，5秒的过期时间
        clients.getIfPresent(clientId);
    }

    @Override
    public Long clientNum() {
        return clients.size();
    }

    @Override
    public ClientInfo getClientInfo() {
        // 获取客户端的信息
        ClientInfo info = new ClientInfo();
        //  只能获取客户端数量？

        info.setClients(clients.size());
        return info;
    }
}
