package com.wxyj.pay.mq;

import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.management.Query;

@Configuration
public class MQConfig {

    @Autowired
    private Environment env;

    /***
     * 创建队列
     * @return
     */
    @Bean
    public Queue orderOrder(){
        return new Queue(env.getProperty("mq.pay.queue.order"));
    }


    /***
     * 创建DirectExchange交换机
     * @return
     */
    @Bean
    public Exchange orderExchange(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.order"), true,false);
    }


    /****
     * 队列绑定到交换机上
     * @return
     */
    @Bean
    public Binding basicBinding(Queue orderQueue,Exchange orderExchange){
        return BindingBuilder.bind(orderQueue).to(orderExchange).with(env.getProperty("mq.pay.routing.key")).noargs();
    }
}
