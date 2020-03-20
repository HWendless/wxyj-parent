package com.wxyj.seckill.controller;

import com.github.pagehelper.PageInfo;
import com.wxyj.seckill.config.TokenDecode;
import com.wxyj.seckill.pojo.SeckillGoods;
import com.wxyj.seckill.service.SeckillGoodsService;
import com.wxyj.seckill.service.SeckillOrderService;
import com.wxyj.seckill.task.MultiThreadingCreateOrder;
import entity.DateUtil;
import entity.Result;
import entity.SeckillStatus;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/****
 * @Author:admin
 * @Description:
 * @Date 2019/6/14 0:18
 *****/

@RestController
@RequestMapping("/seckill/goods")
@CrossOrigin
public class SeckillGoodsController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    MultiThreadingCreateOrder multiThreadingCreateOrder;
    @Autowired
    SeckillOrderService seckillOrderService;



    /****
     * 查询抢购
     * @return
     */
    @RequestMapping(value = "/query")
    public Result queryStatus(String queryStatus){
        //获取用户名
        String username = TokenDecode.getUserInfo().get("username");

        //根据用户名查询用户抢购状态
        SeckillStatus seckillStatus = seckillOrderService.queryStatus(username);

        if(seckillStatus!=null){
            return new Result(true,seckillStatus.getStatus(),"抢购状态");
        }
        //NOTFOUNDERROR =20006,没有对应的抢购数据
        return new Result(false,StatusCode.NOTFOUNDERROR,"没有抢购信息");
    }
//    @RequestMapping(value = "/test")
//    public  void testThread(){
//        multiThreadingCreateOrder.createOrder();;
//        System.out.println("我都不会等你");
//        System.out.println("我执行完了");
//        //调用Service查询商品详情
//
//    }

    /****
     * URL:/seckill/goods/one
     * 根据ID查询商品详情
     * 调用Service查询商品详情
     * @param time
     * @param id
     */
    @RequestMapping(value = "/one")
    public SeckillGoods one(String time,Long id){
        //调用Service查询商品详情
        return seckillGoodsService.one(time,id);
    }

    public  Result<List<Date>> menus(){
        List<Date> deteMenus = DateUtil.getDateMenus();
        return new Result<List<Date>>(true,StatusCode.OK,"查询秒杀时间菜单成功");
    }

    @GetMapping(value = "/list")
    public Result<List<SeckillGoods>> list(String time){
        //调用
        List<SeckillGoods> seckillGoods = seckillGoodsService.list(time);

        return new Result(true,StatusCode.OK,"秒杀商品列表查询成功",seckillGoods);
    }
    /***
     * SeckillGoods分页条件搜索实现
     * @param seckillGoods
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@RequestBody(required = false)  SeckillGoods seckillGoods, @PathVariable  int page, @PathVariable  int size){
        //调用SeckillGoodsService实现分页条件查询SeckillGoods
        PageInfo<SeckillGoods> pageInfo = seckillGoodsService.findPage(seckillGoods, page, size);
        return new Result(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * SeckillGoods分页搜索实现
     * @param page:当前页
     * @param size:每页显示多少条
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result<PageInfo> findPage(@PathVariable  int page, @PathVariable  int size){
        //调用SeckillGoodsService实现分页查询SeckillGoods
        PageInfo<SeckillGoods> pageInfo = seckillGoodsService.findPage(page, size);
        return new Result<PageInfo>(true,StatusCode.OK,"查询成功",pageInfo);
    }

    /***
     * 多条件搜索品牌数据
     * @param seckillGoods
     * @return
     */
    @PostMapping(value = "/search" )
    public Result<List<SeckillGoods>> findList(@RequestBody(required = false)  SeckillGoods seckillGoods){
        //调用SeckillGoodsService实现条件查询SeckillGoods
        List<SeckillGoods> list = seckillGoodsService.findList(seckillGoods);
        return new Result<List<SeckillGoods>>(true,StatusCode.OK,"查询成功",list);
    }

    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable Long id){
        //调用SeckillGoodsService实现根据主键删除
        seckillGoodsService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 修改SeckillGoods数据
     * @param seckillGoods
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody  SeckillGoods seckillGoods,@PathVariable Long id){
        //设置主键值
        seckillGoods.setId(id);
        //调用SeckillGoodsService实现修改SeckillGoods
        seckillGoodsService.update(seckillGoods);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /***
     * 新增SeckillGoods数据
     * @param seckillGoods
     * @return
     */
    @PostMapping
    public Result add(@RequestBody   SeckillGoods seckillGoods){
        //调用SeckillGoodsService实现添加SeckillGoods
        seckillGoodsService.add(seckillGoods);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /***
     * 根据ID查询SeckillGoods数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SeckillGoods> findById(@PathVariable Long id){
        //调用SeckillGoodsService实现根据主键查询SeckillGoods
        SeckillGoods seckillGoods = seckillGoodsService.findById(id);
        return new Result<SeckillGoods>(true,StatusCode.OK,"查询成功",seckillGoods);
    }

    /***
     * 查询SeckillGoods全部数据
     * @return
     */
    @GetMapping
    public Result<List<SeckillGoods>> findAll(){
        //调用SeckillGoodsService实现查询所有SeckillGoods
        List<SeckillGoods> list = seckillGoodsService.findAll();
        return new Result<List<SeckillGoods>>(true, StatusCode.OK,"查询成功",list) ;
    }
}
