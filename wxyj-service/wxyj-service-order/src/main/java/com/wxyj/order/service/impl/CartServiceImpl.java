package com.wxyj.order.service.impl;

import com.wxyj.goods.feign.SkuFeign;
import com.wxyj.goods.feign.SpuFeign;
import com.wxyj.goods.pojo.Sku;
import com.wxyj.goods.pojo.Spu;
import com.wxyj.order.pojo.OrderItem;
import com.wxyj.order.service.CartService;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Override
    public void add(Integer num, Long id, String username) {
        //当添加购物车数量<=0的时候，需要移除该商品信息
        if(num<=0)
        {
            redisTemplate.boundHashOps("Cart_"+username).delete(id);
            //如果此时购物车数量为空，则连购物车一起移除
            Long size = redisTemplate.boundHashOps("Cart_"+username).size();
            if(size==null || size<=0)
            {
                redisTemplate.delete("Cart_" + username);
            }
        }
        //查询SKU
        Result<Sku> resultSku = skuFeign.findById(id+"");
        if(resultSku!=null && resultSku.isFlag()){
            //获取SKU
            Sku sku = resultSku.getData();
            //获取SPU
            Result<Spu> resultSpu = spuFeign.findById(sku.getSpuId());

            //将SKU转换成OrderItem
            OrderItem orderItem = sku2OrderItem(sku,resultSpu.getData(), num);

            /******
             * 购物车数据存入到Redis
             * namespace = Cart_[username]
             * key=id(sku)
             * value=OrderItem
             */
            redisTemplate.boundHashOps("Cart_"+username).put(id,orderItem);
        }

    }

    /***
     * 查询用户购物车数据
     * @param username
     * @return
     */
    @Override
    public List<OrderItem> list(String username) {
        //查询所有购物车数据
        List<OrderItem> orderItems = redisTemplate.boundHashOps("Cart_"+username).values();
        return orderItems;
    }


    /***
     * SKU转成OrderItem
     * @param sku
     * @param num
     * @return
     */
    private OrderItem sku2OrderItem(Sku sku,Spu spu,Integer num){
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(Long.valueOf(sku.getSpuId()));
        orderItem.setSkuId(Long.valueOf(sku.getId()));
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*orderItem.getPrice());       //单价*数量
        orderItem.setPayMoney(num*orderItem.getPrice());    //实付金额
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);           //重量=单个重量*数量

        //分类ID设置
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }
}
