package com.chen.controller;

import com.chen.entity.ReturnT;
import com.chen.entity.vo.ClientRegisterInfo;
import com.chen.entity.vo.ComputeJobResult;
import com.chen.service.impl.ComputeDispenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.chen.service.ClientService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ComputeController {
    @Resource
    private ComputeDispenseServiceImpl computeDispenseServiceImpl;
    @Autowired
    private ClientService clientService;


    /*
    *   Realize client register
    * */
    @RequestMapping("/register")
    public ReturnT register(){
        System.out.println("====注册====");
        ClientRegisterInfo clientRegisterInfo = clientService.register(); //包含客户端的token,just clientId
        // 将成功消息和token发回浏览器保存
        return new ReturnT(ReturnT.SUCCESS_CODE,ReturnT.SUCCESS_MSG,clientRegisterInfo);
    }


    @RequestMapping("/getJob")
    public  ReturnT getJob(@RequestHeader("token") String clientId){
        // 检测客户端是否还在线
        clientService.heart(clientId);
        // 获取任务
        return new ReturnT(ReturnT.SUCCESS_CODE,ReturnT.SUCCESS_MSG,computeDispenseServiceImpl.dispense());
    }

    // submit computed task
    @RequestMapping("postJob")
    public  ReturnT postJob(@RequestBody ComputeJobResult computeJobResult,@RequestHeader("token") String clientId){
        // ensure whether online
        clientService.heart(clientId);
        computeJobResult.setProcessId(clientId);
        computeDispenseServiceImpl.reclaim(computeJobResult);
        return new ReturnT(ReturnT.SUCCESS_CODE,ReturnT.SUCCESS_MSG);
    }

    @RequestMapping("/clientsInfo")
    public  ReturnT getClientInfo(){
        return new ReturnT(ReturnT.SUCCESS_CODE,ReturnT.SUCCESS_MSG,clientService.getClientInfo());
    }


}
