package com.wsz.server.transaction.aspect;

import com.wsz.server.transaction.connection.MyConnection;
import com.wsz.server.transaction.transactional.MyBranchTransaction;
import com.wsz.server.transaction.transactional.MyGlobalTransactionManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Aspect
@Component
public class MyConnectionAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public MyConnection around(ProceedingJoinPoint joinPoint) throws Throwable {
        Connection connection = (Connection) joinPoint.proceed();
        connection.setAutoCommit(false);
        MyBranchTransaction branchTransaction = MyGlobalTransactionManager.getCurrent();
        return new MyConnection(connection, branchTransaction);
    }
}
