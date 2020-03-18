package com.wxyj.user.feign;

import com.wxyj.user.pojo.User;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="user")
@RequestMapping("/user")
public interface UserFeign {

    /**
     * 加载用户的数据
     *
     * @param id
     * @return
     */
    @GetMapping("/load/{id}")
    public Result<User> findByUsername(@PathVariable(name="id") String id);


}
