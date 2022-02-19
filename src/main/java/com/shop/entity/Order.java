package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")//정렬할 때 사용하는 "order" 키워드가 있기 때문에 Order 엔티티에 매핑되는 테이블로 "orders"를 지정한다.
@Getter
@Setter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//부모 엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는
    // CascadeTypeAll 옵션을 설정
    // orphanRemoval 고아 객체 제거 어노테이션 설정 사용
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) { //orderitem에는 주문 상품 정보들을 담아줍니다. orderitem 객체를 order 객체의
        //orderitem에 추가합니다.
        orderItems.add(orderItem);
        orderItem.setOrder(this);//order 엔티티와 orderitem 엔티티가 양방향 참조관계이므로, orderitem객체에도 order 객체를 세팅합니다.
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order(); 
        order.setMember(member); //상품을 주문한 회원의 정보를 세팅합니다.

        for (OrderItem orderItem : orderItemList) { //여러개의 상품을 담을수있도록 리스트형태로 파라미터 값을 받으며 주문 객체에
            //orderitem 객체를 추가합니다.
            order.addOrderItem(orderItem);
        }

        order.setOrderStatus(OrderStatus.ORDER);//주문 상태를 order로 세팅합니다.
        order.setOrderDate(LocalDateTime.now());//현재 시간을 주문 시간으로 세팅합니다.
        return order;
    }

    public int getTotalPrice() { //총 주문 금액을 구하는 메소드
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
