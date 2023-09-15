package com.mungnyang.repository.service;

import com.mungnyang.entity.service.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByMemberMemberId(Long memberId);
}
