package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//JpaRepository 상속받는 ItemRepository를 작성했다. JpaRepository는 2개의 제네릭 타입을 사용한다.
//<첫번째는 엔티티 타입 클래스인 Item과, 기본기 타입인 Long>을 넣어주었다.
//JPA repository에는 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의돼 있다.


//ItemRepository인터페이스를 작성한 것만으로 상품 테이블에 데이터를 insert할 수 있다. Spring Data JPA는 이렇게 인터페이스만 작성하면
//런타임 시점에 자바의 Dynamic Proxy를 이용해서 객체를 동적으로 생성해준다. 따로 Data Access Object(Dao)와 xml 파일에 쿼리문을 작성하지 않아도 된다.
public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    
    //itemNm(상품명)으로 뎅치터를 조회하기 위해 By뒤에 필드명인 itemNm을 메소드의 이름에 붙여줍니다. 엔티티명은 생략이 가능하므로
    //finditemByItemNm 대신 findByItemNm으로 메소드명을 만들어준다. 매개 변수로는 검색할 때 사용할 상품명 변수를 넘겨준다.
    List<Item> findByItemNm(String itemNm);

    //상품을 상품명과 상품 상세설명을 or 조건을 이용하여 조회하는 쿼리 메소드이다.
    List<Item> findByItemNmOrItemDetail(String ItemNm, String itemDetail);
    
    //파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드이다.
    List<Item> findByPriceLessThan(Integer price);

    //상품의 가격이 높은 순으로 조회하는 예제이다.
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //@Query 어노테이션 안에 JPQL로 작성한 쿼리문을 넣는다. from 뒤에는 엔티티 클래스로 작성한 item을 지정해주었고, item으로부터
    //데이터를 select하겠다는 의미이다.
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    //파라미터에 @Param 어노테이션을 이용해 파라미터로 넘어온 값을 JPQL에 들어갈 변수고 지정해준다.
    //현재는 itemDetail 변수를 "like %%"사이에 ":itemDetail"로 값이 들어가도록 작성했다.
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    //value 안에 네이티브 쿼리문을 작성하고 "nativeQuery=true"를 지정합니다.
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
