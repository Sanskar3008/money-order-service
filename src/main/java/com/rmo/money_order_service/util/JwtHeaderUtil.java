package com.rmo.money_order_service.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class JwtHeaderUtil {

    public String getAuthorizationHeader() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) return null;

        return attrs.getRequest().getHeader("Authorization");
    }
}
