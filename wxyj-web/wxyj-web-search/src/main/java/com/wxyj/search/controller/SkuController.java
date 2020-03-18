package com.wxyj.search.controller;
import com.wxyj.search.feign.SkuFeign;
import com.wxyj.search.pojo.SkuInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    /**
     * 搜索
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model){
       // 调用changgou-service-search微服务
        Map resultMap = skuFeign.search(searchMap);
        model.addAttribute("result",resultMap);

        //计算分页
        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.parseLong(resultMap.get("totalPages").toString()),
                Integer.parseInt(resultMap.get("pageNumber").toString())+1,
                        Integer.parseInt(resultMap.get("pageSize").toString())
        );
        //将条件存储用于页面存储
        model.addAttribute("searchMap",searchMap);
//        2个url url：不带排序参数   sorturl：带排序参数

        String [] urls = url (searchMap);
        model.addAttribute("url",urls[0]);
        model.addAttribute("sorturl",urls[1]);

        return "search";
    }

    public String[] url(Map<String,String>  searchMap)
    {
        String url="/search/list";
        String sorturl="/search/list";
        if(searchMap!=null && searchMap.size()>0)
        {
            url+="?";
            sorturl+="?";
            for(Map.Entry<String ,String> entry:searchMap.entrySet())
            {
                if(entry.getKey().equalsIgnoreCase("pageNum"))
                {
                    continue;
                }
                url=entry.getKey()+"="+entry.getValue()+"&";
                if(entry.getKey().equalsIgnoreCase("sortField") || entry.getKey().equalsIgnoreCase("sortRule")){
                    continue;
                }
                sorturl=entry.getKey()+"="+entry.getValue()+"&";

            }
            url=url.substring(0,url.length()-1);
            sorturl=sorturl.substring(0,sorturl.length()-1);
        }



        return new String[]{url,sorturl};
    }
}