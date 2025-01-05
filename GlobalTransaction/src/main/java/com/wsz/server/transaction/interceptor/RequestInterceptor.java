package com.wsz.server.transaction.interceptor;

import com.wsz.server.transaction.transactional.MyGlobalTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String groupId = request.getHeader("groupId");
        String transactionCount = request.getHeader("transactionCount");
        MyGlobalTransactionManager.setCurrentGroupId(groupId);
        MyGlobalTransactionManager.setTransactionCount(Integer.parseInt(transactionCount == null ? "0" : transactionCount));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}