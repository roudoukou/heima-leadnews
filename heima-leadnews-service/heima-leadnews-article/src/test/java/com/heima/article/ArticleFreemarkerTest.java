package com.heima.article;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.pojos.ApArticle;
import com.heima.model.article.pojos.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

@Slf4j
@SpringBootTest
public class ArticleFreemarkerTest {

    @Autowired
    ApArticleContentMapper apArticleContentMapper;

    @Autowired
    Configuration configuration;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ApArticleService apArticleService;

    @Test
    public void createStaticUrlTest() throws IOException, TemplateException {
        // 4.1 获取文章内容
        ApArticleContent apArticleContent = apArticleContentMapper.selectOne(Wrappers.<ApArticleContent>lambdaQuery()
                .eq(ApArticleContent::getId, "1383828014650150914L"));
        if (apArticleContent != null && StringUtils.isNoneBlank(apArticleContent.getContent())) {
            // 4.2 文章内容通过freemarker生成html文件
            Template template = configuration.getTemplate("article.ftl");
            HashMap<String, Object> content = new HashMap<>();
            content.put("content", JSONArray.parseArray(apArticleContent.getContent()));
            StringWriter out = new StringWriter();
            template.process(content, out);

            // 4.3 把html文件上传到minio中
            ByteArrayInputStream inputStream = new ByteArrayInputStream(out.toString().getBytes());
            String path = fileStorageService.uploadHtmlFile("", apArticleContent.getArticleId() + ".html", inputStream);
            // 4.4 修改ap_article表，保存static_url字段
            apArticleService.update(Wrappers.<ApArticle>lambdaUpdate().eq(ApArticle::getId, apArticleContent.getArticleId()).set(ApArticle::getStaticUrl, path));
        }
    }
}
