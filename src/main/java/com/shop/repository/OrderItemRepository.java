package com.shop.repository;

import com.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
//주문 엔티티 조회 테스트 인터페이스
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
        
}
