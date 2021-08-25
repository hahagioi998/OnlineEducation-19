package com.kenProject.eduOrder.controller;


import com.kenProject.commonutils.Result;
import com.kenProject.eduOrder.service.PayLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-08-21
 */
@RestController
@RequestMapping("/eduOrder/pay-log")
@CrossOrigin
public class PayLogController {
    @Autowired
    private PayLogService payLogService;

    //生成微信支付二维码接口
    //参数是订单号
    @GetMapping("createNative/{orderNo}")
    public Result createNative(@PathVariable String orderNo){
        //返回信息，二维码地址，还有其他信息
        Map map =payLogService.createNative(orderNo);
        System.out.println("返回二维码的map集合："+map);
        return Result.ok().data(map);
    }


    //查询订单支付状态
    //参数：订单号，根据订单号查询支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable String orderNo){
        Map<String,String> map=payLogService.queryPayStatus(orderNo);
        System.out.println("订单状态的map集合："+map);
        if (map==null){
            return Result.error().message("支付出错了");
        }
        //如果map不为空，通过map获取订单状态
        if (map.get("trade_state").equals("SUCCESS")){//支付成功
            //添加记录到支付表，更新订单状态
            payLogService.updateOrderStatus(map);
            return Result.ok().message("支付成功");
        }
        return Result.ok().code(25000).message("支付中");
    }


}

