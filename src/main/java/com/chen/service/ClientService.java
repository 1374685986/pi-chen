package com.chen.service;


import com.chen.entity.ClientInfo;
import com.chen.entity.vo.ClientRegisterInfo;

public interface ClientService {

    // 注册
    ClientRegisterInfo register();

    // 检测
    void heart(String clientId);

    // 获取在线机子
    Long clientNum();

    // 获取客户端信息
    ClientInfo getClientInfo();
}
