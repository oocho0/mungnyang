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
            " left join Store s on c.cityId = s.city.cityId" +
            " group by st.stateId")
    List<MainStateDto> findMainStateDtoListForStore();

    @Query("select new com.mungnyang.dto.fixedEntityDto.MainStateDto(st.stateId, st.name, count(a.accommodationId))" +
            " from State st" +
            " left join City c on st.stateId = c.state.stateId" +
            " left join Accommodation a on c.cityId = a.city.cityId" +
            " where a.accommodationStatus != :status or a.accommodationStatus is null" +
            " group by st.stateId")
    List<MainStateDto> findMainStateDtoListForAccommodation(@Param("status") Status closed);
}
