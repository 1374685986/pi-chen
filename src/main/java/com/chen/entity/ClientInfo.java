package com.chen.entity;


import lombok.Data;

import java.util.List;

/*
* 客户端信息
* */
@Data
public class ClientInfo {
    private List<String> clientsID;

    private Long clients;
}
