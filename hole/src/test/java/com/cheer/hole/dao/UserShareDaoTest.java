package com.cheer.hole.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserShareDaoTest {
    @Autowired
    private UserShareDao shareDao;

    @Test
    public void test(){
        System.out.println("启动");
    }
}