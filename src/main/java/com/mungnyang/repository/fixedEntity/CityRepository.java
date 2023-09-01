package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByStateStateIdOrderByCityIdAsc(Long stateId);

    List<City> findAllByOrderByCityIdAsc();

    City findCityByZipcodeStartLessThanEqualAndZipcodeEndGreaterThanEqual(Long zipcode1, Long zipcode2);
}
