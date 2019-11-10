package com.xuecheng.manage_cms.controller;


import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private PageService pageService;
    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size, QueryPageRequest queryPageRequest) {
       /*QueryResult queryResult = new QueryResult();
        queryResult.setTotal(2);
        CmsPage cmsPage = new CmsPage();
        List list = new ArrayList();
        cmsPage.setPageName("测试页");
        list.add(cmsPage);
        queryResult.setList(list);
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;*/
        return pageService.findList(page,size,queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {


        return pageService.add(cmsPage);
    }

    //根据id进行查询当前页面的信息
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id")  String id) {
        System.out.println("find by id:"+id);
        return pageService.getById(id);
    }

    //根据id和传入页面内容进行修改
    @Override
    @PutMapping("/edit/{id}")//这里使用put方法，http 方法中put表示更新
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return pageService.update(id,cmsPage);
    }

    //删除
    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {

        return pageService.delete(id);
    }

    //查询所有的门户id和门户名称
    @RequestMapping("/findAllSites")
    public QueryResponseResult findSiteList(){

        return pageService.findSiteLists();
    }
    //查询所有模板信息
    @RequestMapping("/findThymeleafs")
    public QueryResponseResult findThymeleafLists(){
        return pageService.findThymeleafs();
    }
}
