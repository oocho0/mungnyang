package com.mungnyang.service.fixedEntity;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.fixedEntityDto.MainCityDto;
import com.mungnyang.dto.fixedEntityDto.MainStateDto;
import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.fixedEntity.State;
import com.mungnyang.entity.product.Product;
import com.mungnyang.entity.product.accommodation.Accommodation;
import com.mungnyang.entity.product.store.Store;
import com.mungnyang.repository.fixedEntity.CityRepository;
import com.mungnyang.repository.fixedEntity.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StateCityService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;

    public List<MainStateDto> getMainStateDtoList(Class<? extends Product> product) {
        if (product == Store.class) {
            return stateRepository.findMainStateDtoListForStore();
        }
        return stateRepository.findMainStateDtoListForAccommodation(Status.CLOSED);
    }

    public List<MainCityDto> getMainCityDtoList(Class<? extends Product> product, Long stateId) {
        if (product == Store.class) {
            return cityRepository.findMainCityDtoListForStore(stateId);
        }
        return cityRepository.findMainCityDtoListForAccommodation(stateId, Status.CLOSED);
    }

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
        City findCity = cityRepository.findByZipcode(zipcodeLong, zipcodeLong);
        if (findCity == null) {
            throw new IllegalArgumentException();
        }
        return findCity;
    }
}
