package com.wsz;

import com.wsz.server.transaction.annotation.MyGlobalTransactional;
import com.wsz.server.transaction.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Demo2Service {

    @Autowired
    private Demo2Dao demo2Dao;

    @MyGlobalTransactional(isEnd = true)
    @Transactional
    public void test() {
        User user = new User();
        user.setId(2);
        user.setName("wsz");
        user.setAge(18);
        demo2Dao.insert(user);
    }
}