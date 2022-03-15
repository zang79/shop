package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

//Item 클래스를 entity로 선언하고 @Table 어노테이션으로 어떤 테이블과 매칭될지를 지정한다. item테이블과 매핑되도록 name을 item으로 지정함
@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    /*  entity로 선언한 클래스는 반드시 기본키를 가져야하고, 기본키가 되는 멤버변수에 @Id 어노테이션을 붙여 테이블에 매핑될 컬럼의 이름을 @Column
        어노테이션을 통해 설정한다. item 클래스의 id 변수와 item_id 컬럼이 매핑되도록 하는 것.
        마지막으로 @GenertedValue 어노테이션을 통하여 기본키 생성 전략을 AUTO로 지정한 것.*/
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    //상품 정보로 상품코드,가격,이름,상세설명,판매상태를 만들어주고 판매 상태의 경우 재고가 없거나, 상품이 미리 등록해 놓고 나중에
    private Long id;  //상품코드

/*  @Column 어노테이션의 nullable 속성을 이용해 항상 값이 있어야 하는 필드는 not null을 설정하고
    String 필드는 default 값으로 255가 설정돼 있다. 각 String 필드마다 필요한 길이를 length 속성에 default값을 세팅한 것.*/

    @Column(nullable = false,length = 50)
    private String itemNm; //상품명

    @Column(name="price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    //'판매 중' 상태로 바꾸거나 재고가 없을 때는 프론트에 노출시키지 않기 위해 판매 상태를 코드로 갖고 있는다. 또한 상품을 등록한 시간과
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    //수정한 시간을 상품 테이1블에 기록하기 위해 등록 시간과 수정 시간을 LocalDateTime 타입으로 선언한다.
//    private LocalDateTime regTime; //등록시간

//    private LocalDateTime updateTime; //수정 시간

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber; //재고수량에서 주문 후 남은 재고 수량을 구합니다.
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다.(현재 재고 수량 : " + this.stockNumber + ")");
        } //재고 부족 예외 발생
        this.stockNumber = restStock; //남은 재고 값으로 할당합니다.
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}
