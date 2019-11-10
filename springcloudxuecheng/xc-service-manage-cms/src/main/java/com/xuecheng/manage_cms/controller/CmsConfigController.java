package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.PageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/config")//                   config  conﬁg
public class CmsConfigController {

    @Autowired
    private PageConfigService configService;

    @GetMapping("/getmodel/{id}")
    public CmsConfig getModel(@PathVariable("id") String id){
        //System.out.println("==========================cmsConfiggetById");
        System.out.println("getModel==========="+configService.getConfigById(id));
        return configService.getConfigById(id);
    }
}
