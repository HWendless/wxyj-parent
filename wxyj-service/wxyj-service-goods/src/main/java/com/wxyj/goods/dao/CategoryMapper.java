package com.wxyj.goods.dao;
import com.wxyj.goods.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:admin
 * @Description:Category的Dao
 * @Date 2019/6/14 0:12
 *****/
public interface CategoryMapper extends Mapper<Category> {

    @Select("select tb.* from tb_brand tb, tb_category_brand tcb where tb.id = tcb.brand_id and tcb.category_id = #{pid}")
    List<Category> findByParentId(Integer pid);
}
