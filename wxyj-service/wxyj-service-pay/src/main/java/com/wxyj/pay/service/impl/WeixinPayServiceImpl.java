package com.wxyj.pay.service.impl;

import com.github.wxpay.sdk.WXPayUtil;
import com.wxyj.pay.service.WeixinPayService;
import entity.HttpClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class WeixinPayServiceImpl implements WeixinPayService {
    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.partner}")
    private String partner;

    @Value("${weixin.partnerkey}")
    private String partnerkey;

    @Value("${weixin.notifyurl}")
    private String notifyurl;


    /**
     * 创建二维码
     * @param parameterMap
     * @return
     */

    @Override
    public Map createNative(Map<String, String> parameterMap) {
        try {
        //1、封装参数
        Map param = new HashMap();
        param.put("appid", appid);                              //应用ID
        param.put("mch_id", partner);                           //商户ID号
        param.put("nonce_str", WXPayUtil.generateNonceStr());   //随机数
        param.put("body", "畅购");                            	//订单描述
        param.put("out_trade_no",parameterMap.get("outtradeno"));//商户订单号
        param.put("total_fee",parameterMap.get("total_fee"));//交易金额
        param.put("spbill_create_ip", "127.0.0.1");           //终端IP
        param.put("notify_url", notifyurl);                    //回调地址
        param.put("trade_type", "NATIVE");                     //交易类型

        //2、将参数转成xml字符，并携带签名

            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
