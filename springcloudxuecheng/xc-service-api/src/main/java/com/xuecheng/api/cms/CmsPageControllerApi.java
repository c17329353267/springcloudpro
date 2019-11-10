package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 页面查询接口定义如下：
 */
@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams(
            {@ApiImplicitParam(name="page",value="页 码", required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录 数",required=true,paramType="path",dataType="int")})
    //页面查询，定义返回值类型，封装乐success,message,success_code
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    //定义添加页面的接口
    @ApiOperation("添加页面")//利用swagger来定义接口名称
    public CmsPageResult add(CmsPage cmsPage);

    //根据页面id查询页面
    @ApiOperation("通过id查询页面")
    CmsPage findById(String id);

    //根据页面id进行修改页面
    @ApiOperation("更新界面")
    CmsPageResult edit(String id,CmsPage cmsPage);

    //根据id进行删除界面
    @ApiOperation("删除界面")
    ResponseResult delete(String id);
























}
