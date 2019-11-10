package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    //注册cmsPageFacetory
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Test
    public void testFindPage(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void testRestTemplate(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http;//localhost:31001/cms/config/getmodel/5a7bdd8bd019f1162c63ad32", Map.class);
        System.out.println(forEntity);
    }
}