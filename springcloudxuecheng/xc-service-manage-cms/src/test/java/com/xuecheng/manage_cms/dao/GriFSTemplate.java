package com.xuecheng.manage_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.nio.ch.IOUtil;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GriFSTemplate {

    @Resource
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;
    @Test
    public void testFriFSTemplate() throws FileNotFoundException {
        File file = new File("E:/index_banner.ftl");

        FileInputStream fileInputStream = new FileInputStream(file);

        ObjectId store = gridFsTemplate.store(fileInputStream, "index_banner.ft");
        System.out.println(store);

    }

    //文件的输出
    @Test
    public void testOutPutGridF() throws IOException {
        String fileId = "5a754adf6abb500ad05688d9";

        //根据id查找一个文件
        GridFSFile fsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
        //打开流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        //获取流
        GridFsResource gridFsResource = new GridFsResource(fsFile,gridFSDownloadStream);

        String string = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
       // System.out.println(string);
    }
}
