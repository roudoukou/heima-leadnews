package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.dtos.ArticleHomeDto;
import com.heima.model.article.pojos.ApArticle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApArticleMapper extends BaseMapper<ApArticle> {
    /**
     * 加载文章列表
     * @param dto
     * @param type 1 加载更多 2 加载最新
     * @return
     */
    List<ApArticle> loadArticleList(ArticleHomeDto dto, Short type);
}
