package com.wxyj.goods.feign;


import com.wxyj.goods.pojo.Spu;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="goods")
@RequestMapping(value = "/spu")
public interface SpuFeign {



    /***
     * 根据ID查询Spu数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
     Result<Spu> findById(@PathVariable(value = "id") String id);
}
