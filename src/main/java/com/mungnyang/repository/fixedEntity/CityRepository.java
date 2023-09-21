package com.mungnyang.repository.fixedEntity;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.fixedEntityDto.MainCityDto;
import com.mungnyang.entity.fixedEntity.City;
import com.mungnyang.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByStateStateIdOrderByCityIdAsc(Long stateId);

    List<City> findAllByOrderByCityIdAsc();


    @Query("select c From City c where c.zipcodeStart <= :zipcode1 and c.zipcodeEnd >= :zipcode2")
    City findByZipcode(@Param("zipcode1") Long zipcode1, @Param("zipcode2") Long zipcode2);


    @Query("select new com.mungnyang.dto.fixedEntityDto.MainCityDto(c.cityId, c.name, count(s.storeId))" +
            " from City c" +
            " left outer join Store s on c.cityId = s.city.cityId and s.smallCategory.smallCategoryId in :ids" +
            " where c.state.stateId = :stateId" +
            " group by c.cityId")
    List<MainCityDto> findMainCityDtoListForStore(@Param("stateId") Long stateId, @Param("ids") List<Long> categoryId);
    @Query("select new com.mungnyang.dto.fixedEntityDto.MainCityDto(c.cityId, c.name, count(a.accommodationId))" +
            " from City c" +
            " left outer join Accommodation a on c.cityId = a.city.cityId and a.smallCategory.smallCategoryId in :ids" +
            " where c.state.stateId = :stateId and (a.accommodationStatus != :status or a.accommodationStatus is null)" +
            " group by c.cityId")
    List<MainCityDto> findMainCityDtoListForAccommodation(@Param("stateId") Long stateId, @Param("status") Status closed, @Param("ids") List<Long> categoryId);
}
