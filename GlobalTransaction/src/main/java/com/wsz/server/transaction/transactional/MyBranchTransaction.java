package com.wsz.server.transaction.transactional;


public class MyBranchTransaction {

    private String groupId;
    private String transactionId;
    private TransactionStatus transactionStatus;   // commit-待提交，rollback-待回滚
    private Object lock;

    public MyBranchTransaction(String groupId, String transactionId) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.lock = new Object();
    }

    public MyBranchTransaction(String groupId, String transactionId, TransactionStatus transactionStatus) {
        this.groupId = groupId;
        this.transactionId = transactionId;
        this.transactionStatus = transactionStatus;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Object getLock() {
        return lock;
    }

    public void setLock(Object lock) {
        this.lock = lock;
    }
}