package com.xuecheng.manage_cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PageServiceTest {


    @Autowired
    PageService pageService;

    @Test
    public void testGetPageHtml(){
        String pageHtml = pageService.getPageHtml("5dc197a967753535a81a0779");//5dc4da43f44ac545457294d2
        System.out.println(pageHtml);

    }

}