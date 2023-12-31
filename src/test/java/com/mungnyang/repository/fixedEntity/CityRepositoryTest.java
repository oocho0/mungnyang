package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CityRepositoryTest {


    @Autowired
    private  CityRepository cityRepository;

    @Test
    @DisplayName("zipcode로 City 찾기")
    void zipcode를_넣으면_올바른_City를_반환한다(){
        String test = "1234";
        Long encodingTest = Long.parseLong(test);
        City city = cityRepository.findByZipcode(encodingTest, encodingTest);
        assertThat(city.getName()).isEqualTo("강북구");
    }

}