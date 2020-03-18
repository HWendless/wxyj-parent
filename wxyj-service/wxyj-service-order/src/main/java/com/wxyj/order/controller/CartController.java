package com.wxyj.order.controller;

import com.wxyj.order.config.TokenDecode;
import com.wxyj.order.pojo.OrderItem;
import com.wxyj.order.service.CartService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    /***
     * 加入购物车
     * @param num:购买的数量
     * @param id：购买的商品(SKU)ID
     * @return
     */
    @RequestMapping(value = "/add")
    public Result add(Integer num, Long id){
        //用户名
        //获取用户登录信息
        Map<String, String> userMap = TokenDecode.getUserInfo();
        String username = userMap.get("username");
        //将商品加入购物车
        cartService.add(num,id,username);
        return new Result(true, StatusCode.OK,"加入购物车成功！");
    }


    /***
     * 查询用户购物车列表
     * @return
     */
    @GetMapping(value = "/list")
    public Result list(){
        //用户名
        //获取用户登录信息
        Map<String, String> userMap = TokenDecode.getUserInfo();
        String username = userMap.get("username");
        List<OrderItem> orderItems = cartService.list(username);
        return new Result(true,StatusCode.OK,"购物车列表查询成功！",orderItems);
    }



}
