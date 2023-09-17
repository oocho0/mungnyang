package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.BigCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BigCategoryRepositoryTest {

    @Autowired
    private BigCategoryRepository bigCategoryRepository;

    @Test
    @DisplayName("지정한 대분류 아이디를 제외한 대분류 모두 찾기")
    void 편의시설에_사용될_대분류_리스트에는_숙소만빼고_다있음(){
        List<BigCategory> bigCategories = bigCategoryRepository.findByBigCategoryIdNotOrderByBigCategoryIdAsc(1L);
        List<String> bigCategoryNames = new ArrayList<>();
        for (BigCategory bigCategory : bigCategories) {
            bigCategoryNames.add(bigCategory.getName());
        }
        assertThat(bigCategoryNames).contains("놀이");
        assertThat(bigCategoryNames).contains("문화");
        assertThat(bigCategoryNames).contains("카페");
        assertThat(bigCategoryNames).contains("애견 카페");
        assertThat(bigCategoryNames).contains("식당");
        assertThat(bigCategoryNames).doesNotContain("숙소");
    }
}