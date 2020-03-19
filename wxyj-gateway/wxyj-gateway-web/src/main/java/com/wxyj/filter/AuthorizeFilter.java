package com.wxyj.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

public class AuthorizeFilter implements GlobalFilter,Ordered {

    private static final  String AUTHORIZE_TOKEN = "Authorization";

    /**
     * 全局拦截
     * @param exchange
     * @param chain
     * @return
     */

    //拦截
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request =exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //获取用户令牌信息
        //1)头文件中
        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        String Path = request.getURI().toString();
        boolean hasToken = true;

        if(URLFilter.hasAuthorize(Path))
        {

        }

        //2)如果头文件中没有，则从请求参数中获取
        if (StringUtils.isEmpty(token)) {
            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            hasToken = false;
        }

        //3)Cookie中
        if(StringUtils.isEmpty(token))
        {
            HttpCookie httpCookie = request.getCookies().getFirst(AUTHORIZE_TOKEN);
            if(httpCookie != null)
            {
                token = httpCookie.getValue();
            }
        }

        //如果为空，则输出错误代码
        if (StringUtils.isEmpty(token)) {
            //设置方法不允许被访问，405错误代码
            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            return response.setComplete();
        }
        //判断令牌是否为空，如果不为空，将令牌头文件中
        //如果有令牌，则校验令牌是否有效
//        try {
//
//         JwtUtil.parseJWT(token);
//
//
//        } catch (Exception e) {
//            //无效拦截
//            response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
//            return response.setComplete();
//
//        }
        //如果令牌为空，则不允许，直接拦截
        if(StringUtils.isEmpty(token))
        {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }else {
            if(!token.startsWith("bearer") && !token.startsWith("bearer"))
            {
                token = "bearer " + token;
            }
        }
        if(!hasToken)
        {
            //将令牌封装到头文件中
            request.mutate().header(AUTHORIZE_TOKEN,token);
        }

        //有效放行
        return chain.filter(exchange);
    }

    //排序，越小越先执行
    @Override
    public int getOrder() {
        return 0;
    }
}
