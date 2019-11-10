package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsThymeleafRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private CmsThymeleafRepository cmsThymeleafRepository;

     public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //判断queryPageRequest是否为空，为空则创建对象
         if(queryPageRequest == null){
             queryPageRequest = new QueryPageRequest();
         }
         //创建匹配器
         ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                 .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());//模糊匹配页面别名
         CmsPage cmsPage = new CmsPage();
         //赋值站点id
         if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
             cmsPage.setSiteId(queryPageRequest.getSiteId());
         }
         if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
             //如果页面请求中的页面别名不为空
             cmsPage.setPageAliase(queryPageRequest.getPageAliase());
         }

         //定义example
         Example<CmsPage> example = Example.of(cmsPage,exampleMatcher);
         //查询所有封装到界面


         if(page <= 0){
             page = 1;
         }
         page = page -1;
         if(size <= 0){
             size = 10;
         }
         //创建分页对象
         Pageable pageable = PageRequest.of(page, size);
         Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
         QueryResult<CmsPage> queryResult = new QueryResult<>();
         queryResult.setList(all.getContent());//数据列表
         queryResult.setTotal(all.getTotalElements());//数据中记录数
         QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
         return  queryResponseResult;
     }

     //查询所有的门户信息
     public QueryResponseResult findSiteLists(){
         List<CmsSite> all = cmsSiteRepository.findAll();
         QueryResult<CmsSite> queryResult = new QueryResult<>();
         queryResult.setList(all);
         QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
         return queryResponseResult;
     }

     public CmsPageResult add(CmsPage cmsPage){
         CmsPage cmspage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

         if(cmsPage != null){
             ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
         }
         if(cmspage1 == null){
             cmsPage.setPageId(null);
             cmsPageRepository.save(cmsPage);
             //返回值结果
             CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS,cmsPage);
             return cmsPageResult;
         }
         return new CmsPageResult(CommonCode.FAIL,null);
     }

     //查询所有的模板信息
     public QueryResponseResult findThymeleafs(){
         List<CmsTemplate> all = cmsThymeleafRepository.findAll();
         QueryResult<CmsTemplate> queryResult = new QueryResult<>();
         queryResult.setList(all);
         return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
     }
     //根据id进行页面信息查询
    public CmsPage getById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
         if(optional.isPresent()){//如果查询的结果不为空
             return optional.get();
         }
         return null;
    }

    //更新页面信息
    public CmsPageResult update(String id,CmsPage cmsPage){
         CmsPage one = this.getById(id);
         if(one !=null){
             //准备更新数据
             //设置要修改的数据
             //更新模板id
             one.setTemplateId(cmsPage.getTemplateId());
             //更新所属站点
             one.setSiteId(cmsPage.getSiteId());
             //更新页面别名
             one.setPageAliase(cmsPage.getPageAliase());
             //更新页面名称
             one.setPageName(cmsPage.getPageName());
             //更新访问路径
             one.setPageWebPath(cmsPage.getPageWebPath());
             //更新物理路径
             one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
             //
             one.setDataUrl(cmsPage.getDataUrl());
             //提交修改
             cmsPageRepository.save(one);
             return new CmsPageResult(CommonCode.SUCCESS,one);
        }
         return new CmsPageResult(CommonCode.FAIL,one);
    }

    //删除界面
    public ResponseResult delete(String id){
         //先查询出待删除的界面
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if(optional.isPresent()){
            cmsPageRepository.deleteById(id);
            return  new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    RestTemplate restTemplate;
    //实现页面静态化
    public String getPageHtml(String pageId){
         //根据页面id拿到页面模板

        //根据页面id拿到页面数据
        Map model = getModelByPageId(pageId);
        if(model == null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        String template = getTemplateByPageId(pageId);
        if(StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //执行页面静态化
        String html = generateHtml(template,model);
        return html;
    }
    //执行静态化
    private String generateHtml(String templateContent,Map model ){
        //创建配置对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //船舰模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        //configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template = configuration.getTemplate("template");
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template,model);
            return content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取页面的模板信息
    private String getTemplateByPageId(String pageId){
        CmsPage cmspage = getById(pageId);
        if(cmspage == null){

            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //从页面信息中获取模板id
        String templateId = cmspage.getTemplateId();
        if(StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询模板信息
        Optional<CmsTemplate> optional = cmsThymeleafRepository.findById(templateId);
        if(optional.isPresent()){//不为空
            //获取cms模板
            CmsTemplate cmsTemplate = optional.get();
            //根据模板获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //根据文件id查询文件
            GridFSFile gridfsfile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridfsfile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridfsfile,gridFSDownloadStream);
            //从流中获取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //拿到页面的数据模型
    private Map getModelByPageId(String pageId){
        CmsPage cmspage = getById(pageId);
        if(cmspage == null){
            //页面不存在
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmspage.getDataUrl();
        if(StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return  body;

    }
}