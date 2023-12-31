package com.shiro.demo.util;

import com.alibaba.fastjson.JSON;
import com.shiro.demo.bean.CommonResp;
import com.shiro.demo.context.AuthContext;
import com.shiro.demo.enums.ErrorCodeEnum;
import com.shiro.demo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Slf4j
public class ClearUtil {

    public static void cleanup(ServletResponse response, Exception existing)
            throws IOException {
        AuthContext.clear();
        if (null != existing) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            if (existing instanceof BusinessException) {
                BusinessException businessException = (BusinessException) existing;
                response.getWriter().write(JSON.toJSONString(CommonResp.fail(businessException.getCode(), businessException.getMessage())));
            } else {
                log.error("system error", existing);
                response.getWriter().write(JSON.toJSONString(CommonResp.fail(ErrorCodeEnum.CODE_9999, ErrorCodeEnum.CODE_9999.getMessage(), existing.getMessage())));
            }
        }
    }
}
