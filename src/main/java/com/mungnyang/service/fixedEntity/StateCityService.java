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

    public List<State> getAllStates(){
        return stateRepository.findAllByOrderByStateIdAsc();
    }

    public List<City> getAllCities() {
        return cityRepository.findAllByOrderByCityIdAsc();
    }

    public List<City> getCitiesByStateId(Long stateId){
        return cityRepository.findByStateStateIdOrderByCityIdAsc(stateId);
    }

    public City getMatchedCity(String zipcode){
        List<City> cities = getAllCities();
        int zipcodeInt = Integer.parseInt(zipcode);
        for (City city : cities) {
            if (zipcodeInt >= city.getZipcodeStart() && zipcodeInt < city.getZipcodeEnd()) {
                return city;
            }
        }
        return null;
    }

}
