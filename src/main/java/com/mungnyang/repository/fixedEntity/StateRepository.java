package com.mungnyang.repository.fixedEntity;

import com.mungnyang.constant.Status;
import com.mungnyang.dto.fixedEntityDto.MainStateDto;
import com.mungnyang.entity.fixedEntity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {

    List<State> findAllByOrderByStateIdAsc();

    @Query("select new com.mungnyang.dto.fixedEntityDto.MainStateDto(st.stateId, st.name, count(s.storeId))" +
            " from State st" +
            " left join City c on st.stateId = c.state.stateId" +
            " left outer join Store s on c.cityId = s.city.cityId and s.smallCategory.smallCategoryId in :ids" +
            " group by st.stateId")
    List<MainStateDto> findMainStateDtoListForStore(@Param("ids") List<Long> smallCategoryId);

    @Query("select new com.mungnyang.dto.fixedEntityDto.MainStateDto(st.stateId, st.name, count(a.accommodationId))" +
            " from State st" +
            " left join City c on st.stateId = c.state.stateId" +
            " left outer join Accommodation a on c.cityId = a.city.cityId and a.smallCategory.smallCategoryId in :ids" +
            " where a.accommodationStatus != :status or a.accommodationStatus is null" +
            " group by st.stateId")
    List<MainStateDto> findMainStateDtoListForAccommodation(@Param("status") Status closed, @Param("ids") List<Long> smallCategoryId);
}
