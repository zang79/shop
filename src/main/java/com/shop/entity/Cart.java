package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name= "cart")
@Getter
@Setter
@ToString
//일대일 단방향 매핑하기
/*4장에서 만든 장바구니(Cart) 엔티티를 만들고 회원 엔티티와 연관 관계 매핑을 설정*/
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // OneToOne 어노테이션 일대일 매핑
    @JoinColumn(name = "member_id")  /*@JoinColumn 어노테이션 매핑할 외래키를 지정하고 name 속성에는 매핑할 외래키의 이름을 설정.
    @JoinColumn의 name을 명시하지 않으면 JPA가 알아서 ID를 찾지만 컬럼명이 원하는 대로 생성되지 않을 수 있기 때문에 직접 저장한다.*/
    private Member member;
}
