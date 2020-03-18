package com.wxyj.goods.feign;
import com.wxyj.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name="goods")
@RequestMapping(value = "/sku")
public interface SkuFeign {

    /***
     * 根据ID查询Sku数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    Result<Sku> findById(@PathVariable("id") String id);


    /***
     * 根据审核状态查询Sku
     * @param status
     * @return
     */
    @GetMapping("/status/{status}")
    Result<List<Sku>> findByStatus(@PathVariable("status") String status);

    /**
     * 商品信息递减
     * Map<key,value> key:要递减的商品ID
     *                value:要递减的数量
     * @param decrmap
     * @return
     */
    @GetMapping(value = "/decr/count")
    Result decrCount(@RequestParam Map<String,Integer> decrmap);
}