package com.mungnyang.repository.fixedEntity;

import com.mungnyang.entity.fixedEntity.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StateRepository extends JpaRepository<State, Long> {

    List<State> findAllByOrderByStateIdAsc();
}
