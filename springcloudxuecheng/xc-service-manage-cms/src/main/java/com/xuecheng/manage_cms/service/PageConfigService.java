package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.model.response.Response;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageConfigService {

    @Autowired
    private CmsPageConfigRepository cmsPageConfigRepository;

    //根据id查询配置集合(monogodb称为的集合)
    public CmsConfig getConfigById(String id){
        Optional<CmsConfig> option = cmsPageConfigRepository.findById(id);
        if(option.isPresent()){
            CmsConfig cmsConfig = option.get();
            return cmsConfig;
        }
        return null;
    }
}
