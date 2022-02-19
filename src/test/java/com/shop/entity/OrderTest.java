package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class OrderTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){

        Order order = new Order();

        for (int i = 0; i < 3; i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem); // 영속성 컨텍스트에 저장되지 않은 orderitem 엔티티를 order 에닡티에 담는다.
        }

        orderRepository.saveAndFlush(order); // order 엔티티를 저장하면서 강제로 flush를 호출해 영속성 컨텍스트에 있는 객체들을 db에 반영한다.
        em.clear(); // 영속성 컨텍스트의 상태를 초기화한다.

        Order savedOrder = orderRepository.findById(order.getId()) // 영속성 컨텍스트를 초기화 했기 때문에 데이터베이스에서
                // 주문 엔티티를 조회하고, select 쿼리문이 실행되는 것을 콘솔에서 확인 가능하다.
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder(){//주문데이터를 생성해 저장함 > 고아객체준비
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }
    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0); // order 엔티티에서 관리하고있는 orderItem 리스트의 0번째 인덱스 요소 제거
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest(){
        Order order = this.createOrder(); // 기존 만들었던 주문 생성 메소드를 이용하여 주문 데이터를 저장 함
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId) // 영속성 컨텍스트의 상태 초기화 후 order 엔티티에
                // 저장했던 주문 상품 아이디를 이용하여 orderitem 을 데이터 베이스에서 다시 조회한다.
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass()); // orderItem 엔티티에 있는 order 객체의
        //클래스를 출력합니다. Order 클래스가 출력되는 것을 확인할 수 있다.
        System.out.println("================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("================================");
        //지연로딩이 될경우 HibernateProxy라하여 실제 엔티티 대신에 프록시 객체를 넣어준다.
    }
}
