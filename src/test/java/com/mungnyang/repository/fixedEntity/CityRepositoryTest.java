package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CityRepositoryTest {


    @Autowired
    private  CityRepository cityRepository;

    @Test
    @DisplayName("zipcode로 City 찾기")
    void zipcode를_넣으면_올바른_City를_반환한다(){
        City city = cityRepository.findCityByZipcodeStartLessThanEqualAndZipcodeEndGreaterThanEqual(7072L, 7072L);
        assertThat(city.getName()).isEqualTo("동작구");
    }

}