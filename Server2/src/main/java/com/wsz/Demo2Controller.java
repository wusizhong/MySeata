package com.wsz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("server2")
public class Demo2Controller {

    @Autowired
    private Demo2Service demo2Service;

    @RequestMapping(value = "test")
    public void test() {
        System.out.println("server2 test.......");
        demo2Service.test();
    }
}