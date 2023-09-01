package com.mungnyang.service.fixedEntity;

import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.fixedEntity.State;
import com.mungnyang.repository.fixedEntity.CityRepository;
import com.mungnyang.repository.fixedEntity.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StateCityService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    /**
     * 모든 시/도를 id 순으로 반환
     * @return 모든 시/도 리스트
     */
    public List<State> getAllStates(){
        return stateRepository.findAllByOrderByStateIdAsc();
    }

    public List<City> getAllCities() {
        return cityRepository.findAllByOrderByCityIdAsc();
    }

    public List<City> getCitiesByStateId(Long stateId){
        return cityRepository.findByStateStateIdOrderByCityIdAsc(stateId);
    }

    /**
     * zipcode에 맞는 City 찾기
     * @param zipcode 찾을 zipcode 값
     * @return 찾은 City
     */
    public City getMatchedCity(String zipcode){
        Long zipcodeLong = Long.parseLong(zipcode);
        return cityRepository.findCityByZipcodeStartLessThanEqualAndZipcodeEndGreaterThanEqual(zipcodeLong, zipcodeLong);
    }

}
