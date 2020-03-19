package com.wxyj.order.listener;

import com.alibaba.fastjson.JSON;
import com.wxyj.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

@Component
@RabbitListener(queues = {"${mq.pay.queue.order}"})
public class OrderPayMessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;



    /***
     * 接收消息
     */
    @RabbitHandler
    public void consumeMessage(String msg) throws ParseException {
        //将数据转成Map
        Map<String,String> result = JSON.parseObject(msg,Map.class);

        //return_code=SUCCESS
        String return_code = result.get("return_code");
        //业务结果
        String result_code = result.get("result_code");

        //业务结果 result_code=SUCCESS/FAIL，修改订单状态
        if(return_code.equalsIgnoreCase("success") ){
            //获取订单号
            String outtradeno = result.get("out_trade_no");
            //业务结果
            if(result_code.equalsIgnoreCase("success")){
                if(outtradeno!=null){
                    //修改订单状态  out_trade_no
                    orderService.upadataStatus(outtradeno,result.get("time_end").toString(),result.get("transaction_id"));
                }
            }else{
               // 支付失败，关闭支付，取消订单，回滚库存
                orderService.deleteOrder(outtradeno);
            }
        }

    }
}