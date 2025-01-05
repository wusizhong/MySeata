package com.wsz;

import com.wsz.server.transaction.annotation.MyGlobalTransactional;
import com.wsz.server.transaction.util.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    @MyGlobalTransactional(isStart = true)
    @Transactional
    public void test() {
        User user = new User();
        user.setId(1);
        user.setName("wss");
        user.setAge(17);
        demoDao.insert(user);
        HttpClient.get("http://localhost:8082/server2/test");
//        int i = 100/0;
    }
}