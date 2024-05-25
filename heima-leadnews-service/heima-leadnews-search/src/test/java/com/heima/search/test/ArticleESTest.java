package com.heima.search.test;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.search.dtos.UserSearchDto;
import com.heima.search.SearchApplication;
import com.heima.search.service.ApAssociateWordsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class ArticleESTest {

    @Autowired
    ApAssociateWordsService apAssociateWordsService;

    @Test
    public void testSearchPattern(){
        UserSearchDto userSearchDto = new UserSearchDto();
        userSearchDto.setSearchWords("黑马");
        ResponseResult associate = apAssociateWordsService.findAssociate(userSearchDto);
        System.out.println(associate.getData());
    }

    @Test
    public void testSearchAll(){
        ResponseResult associate = apAssociateWordsService.findAll();
        System.out.println(associate.getData());
    }

}
