package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByStateStateIdOrderByCityIdAsc(Long stateId);

    List<City> findAllByOrderByCityIdAsc();


    @Query("select c From City c where c.zipcodeStart <= :zipcode1 and c.zipcodeEnd >= :zipcode2")
    City findByZipcode(@Param("zipcode1") Long zipcode1, @Param("zipcode2") Long zipcode2);
}
