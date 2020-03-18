package com.wxyj.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /**
     * 创建二维码
     * @param parameterMap
     * @return
     */
    Map createNative(Map<String,String> parameterMap) ;
}
