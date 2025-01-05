package com.wsz.server.transaction.transactional;

import com.alibaba.fastjson.JSONObject;
import com.wsz.server.transaction.netty.NettyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class MyGlobalTransactionManager {


    private static NettyClient nettyClient;

    private static ThreadLocal<MyBranchTransaction> current = new ThreadLocal<>();
    private static ThreadLocal<String> currentGroupId = new ThreadLocal<>();
    private static ThreadLocal<Integer> transactionCount = new ThreadLocal<>();

    @Autowired
    public void setNettyClient(NettyClient nettyClient) {
        MyGlobalTransactionManager.nettyClient = nettyClient;
    }

    public static Map<String, MyBranchTransaction> BRANCH_TRANSACION_MAP = new HashMap<>();

    /**
     * 创建事务组，并且返回groupId
     *
     * @return
     */
    public static String createTransactionGroup() {
        String groupId = UUID.randomUUID().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", groupId);
        jsonObject.put("command", "create");
        nettyClient.send(jsonObject);
        System.out.println("创建事务组");

        currentGroupId.set(groupId);
        return groupId;
    }

    /**
     * 创建子事务
     *
     * @param groupId
     * @return
     */
    public static MyBranchTransaction createBranchTransaction(String groupId) {
        String transactionId = UUID.randomUUID().toString();
        MyBranchTransaction myBranchTransaction = new MyBranchTransaction(groupId, transactionId);
        BRANCH_TRANSACION_MAP.put(groupId, myBranchTransaction);
        current.set(myBranchTransaction);
        addTransactionCount();

        System.out.println("创建分支事务");

        return myBranchTransaction;
    }

    /**
     * 注册分支事务
     *
     * @param myBranchTransaction
     * @param isEnd
     * @param transactionStatus
     * @return
     */
    public static MyBranchTransaction registerBranchTransaction(MyBranchTransaction myBranchTransaction, Boolean isEnd, TransactionStatus transactionStatus) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupId", myBranchTransaction.getGroupId());
        //可选
        jsonObject.put("transactionId", myBranchTransaction.getTransactionId());
        jsonObject.put("transactionStatus", transactionStatus);
        jsonObject.put("command", "add");
        jsonObject.put("isEnd", isEnd);
        jsonObject.put("transactionCount", MyGlobalTransactionManager.getTransactionCount());
        nettyClient.send(jsonObject);
        System.out.println("注册分支事务");
        return myBranchTransaction;
    }

    public static MyBranchTransaction getTransaction(String groupId) {
        return BRANCH_TRANSACION_MAP.get(groupId);
    }

    public static MyBranchTransaction getCurrent() {
        return current.get();
    }

    public static String getCurrentGroupId() {
        return currentGroupId.get();
    }

    public static void setCurrentGroupId(String groupId) {
        currentGroupId.set(groupId);
    }

    public static Integer getTransactionCount() {
        return transactionCount.get();
    }

    public static void setTransactionCount(int i) {
        transactionCount.set(i);
    }

    public static void addTransactionCount() {
        int i = (transactionCount.get() == null ? 0 : transactionCount.get()) + 1;
        transactionCount.set(i);
    }
}