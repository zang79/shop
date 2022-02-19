package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    @QueryProjection //query로 결과 조회시 mainitemdto 객체로 바로 받아오도록 활용하겠습니다.
    public MainItemDto(Long id, String item, String itemDetail, String imgUrl, Integer price) {
        this.id=id;
        this.itemNm=item;
        this.imgUrl=imgUrl;
        this.price=price;
    }
}
