package com.wxyj.oauth.intercerceptor;

import com.wxyj.oauth.util.AdminToken;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenRequestInterceptor implements RequestInterceptor {

    /**
     * fegin执行之前进行拦截
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        /**
         * 从数据库加载查询用户信息
         * 1.没有令牌，feign调用之前，生成令牌（admin）
         * 2：Feign调用之前，令牌需要携带过去
         * 3:Feign调用之前，令牌需要存放到Header文件中
         * 4：请求->F
         */
        String token = AdminToken.adminToken();
        template.header("Authorization","bearer " + token);

    }
}
