package com.chen.controller;

import com.chen.entity.ReturnT;
import com.chen.entity.vo.ClientRegisterInfo;
import com.chen.entity.vo.ComputeJobResult;
import com.chen.service.impl.ComputeDispenseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import com.chen.service.ClientService;
import com.chen.service.ComputeDispenseService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ComputeController {
    @Resource
    private ComputeDispenseServiceImpl computeDispenseServiceImpl;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/register")
    public ReturnT register(){
        System.out.println("====注册====");
        ClientRegisterInfo clientRegisterInfo = clientService.register();
        return new ReturnT(ReturnT.SUCCESS_CODE,ReturnT.SUCCESS_MSG,clientRegisterInfo);
    }
    @RequestMapping("/getJob")
    public  ReturnT getJob(@RequestHeader("token") String clientId){
        clientService.heart(clientId);
        return new ReturnT(ReturnT.SUCCESS_CODE,ReturnT.SUCCESS_MSG,computeDispenseServiceImpl.dispense());
    }

    @RequestMapping("postJob")
    public  ReturnT postJob(@RequestBody ComputeJobResult computeJobResult,@RequestHeader("token") String clientId){
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
