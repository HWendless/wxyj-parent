package com.wxyj.order.queue;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfig {
    /**
     * 创建Queue1 延时队列，会过期，过期后将数据发给Queue2
     */
    //@Bean中的name属性如果不写默认是方法名
    @Bean
    public Queue orderDelayQueue(){
        return  new Queue("orderDelayMessage",true);
    }
    /**
     * 创建Queue2
     */
    @Bean
    public Queue orderListenerQueue(){
        return QueueBuilder.durable("orderListenerQueue")
            .withArgument("x-dead-letter-exchange", "orderListenerExchange") // 消息超时进入死信队列，绑定死信队列交换机
            .withArgument("x-dead-letter-routing-key", "orderListenerQueue")   // 绑定指定的routing-key
            .build();
    }

    /**
     * 创建交换机
     */
    @Bean
    public Exchange orderListenerExchange()
    {
        return  new DirectExchange("orderListenerExchange");
    }

    /**
     * 队列queue2绑定Exchange
     */
    @Bean
    public Binding orderListenerBinding(Queue orderListenerQueue,Exchange orderListenerExchange)
    {
        return BindingBuilder.bind(orderListenerQueue).to(orderListenerExchange).with("orderListenerQueue").noargs();
    }



}
