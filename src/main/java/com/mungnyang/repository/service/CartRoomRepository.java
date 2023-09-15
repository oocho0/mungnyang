package com.mungnyang.repository.service;

import com.mungnyang.entity.service.CartRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRoomRepository extends JpaRepository<CartRoom, Long> {

    List<CartRoom> findByCartCartId(Long cartId);
}
