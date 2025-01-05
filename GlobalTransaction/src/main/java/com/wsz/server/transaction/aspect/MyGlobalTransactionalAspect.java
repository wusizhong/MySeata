package com.wsz.server.transaction.aspect;

import com.wsz.server.transaction.annotation.MyGlobalTransactional;
import com.wsz.server.transaction.transactional.MyBranchTransaction;
import com.wsz.server.transaction.transactional.MyGlobalTransactionManager;
import com.wsz.server.transaction.transactional.TransactionStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Aspect
@Component
public class MyGlobalTransactionalAspect implements Ordered {

   @Around("@annotation(com.wsz.server.transaction.annotation.MyGlobalTransactional)")
   public void around(ProceedingJoinPoint joinPoint) {
       MethodSignature signature = (MethodSignature) joinPoint.getSignature();
       Method method = signature.getMethod();
       MyGlobalTransactional annotation = method.getAnnotation(MyGlobalTransactional.class);
       boolean isStart = annotation.isStart();

       String transactionGroup;
       if (isStart) {
           //创建全局事务
           transactionGroup = MyGlobalTransactionManager.createTransactionGroup();
       }else {
           transactionGroup = MyGlobalTransactionManager.getCurrentGroupId();
       }

       //创建分支事务
       MyBranchTransaction branchTransaction = MyGlobalTransactionManager.createBranchTransaction(transactionGroup);

       //注册分支事务
       try {
           joinPoint.proceed();
           MyGlobalTransactionManager.registerBranchTransaction(branchTransaction, annotation.isEnd(), TransactionStatus.commit);
       } catch (Throwable e) {
           MyGlobalTransactionManager.registerBranchTransaction(branchTransaction, annotation.isEnd(), TransactionStatus.rollback);
           throw new RuntimeException(e);
       }
   }

    @Override
    public int getOrder() {
        return 1000000;
    }
}
