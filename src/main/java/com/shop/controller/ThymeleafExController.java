package com.shop.controller;

import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
//클라이언트의 요청에 대해 어떤 컨트롤러가 처리할지 매핑하는 어노테이션이다. url에 "/thymeleaf"경로로 오는 요청을 ThymeleafExController가 처리하도록 한다.
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model) {
        //model 객체를 이용해 뷰에 전달한 데이터를 key, value 구조로 넣어준다
        model.addAttribute("data", "타임리프 예제 입니다.");
        //templates 폴더를 기준으로 뷰의 위치와 이름(thymeleafEx01.html)을 반환한다.
        return "thymeleafEx/thymeleafEx01";
    }
    @GetMapping(value = "/ex02")
    public String thymeleafExample02(Model model) {
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    //여러 개의 데이터를 가지고 있는 컬렉션 데이터를 화면에 출력하는 방법
    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model) {

        List<ItemDto> itemDtoList = new ArrayList<>();

        //반복문을 통한 출력할 10개의 itemDto 객체를 만들어서 itemDtoList에 넣는다
        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);

        }
        //화면에서 출력할 itemDtoList를 model에 담아서 view에 전달한다.
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx03";
    }

    // 124p th:if , th:unless예제
    // 예제 순번이 짝수면 '짝수'를 출력하고 홀수면 '홀수를 출력해주는 예제 if else 조건이라 생각하면 됌
    @GetMapping(value = "/ex04")
    public String thymeleafExample04(Model model) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        //반복문을 통한 출력할 10개의 itemDto 객체를 만들어서 itemDtoList에 넣는다
        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명" + i);
            itemDto.setItemNm("테스트 상품" + i);
            itemDto.setPrice(1000 * i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        //Thymeleaf에서는 링크를 처리하는 문법으로 th:href가 있다. 링크의 종류는 'Absolute URL'과 'Context-relativ URL'이 있다.
        //이동할 서버의 URL을 입력해주는 Absolute URL 방식은 'http://' 또는 'https://'로 시작한다
        //Context-relativ URL은 가장 많이 사용되는 URL 형식이며 우리가 실행하는 애플리케이션의 서버 내부를 이동하는 방법이라고 생각면 된다.
        //웹 애플리케이션 루트에 상대적인 URL을 입력, 상대경로는 .URL 프로토콜이나 호스트 이름을 지정하지 않는다.
        return "thymeleafEx/thymeleafEx05";
    }

    @GetMapping(value = "/ex06")
    //전달했던 매개 변수와 같은 이름의 String 변수 param1, param2를 파라미터로 설정하면 자동으로 데이터가 바인딩 된다. 매개 변수를 model에 담아서 View로 전달
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value = "/ex07")
    //133p Thymeleaf 페이지 레이아웃웃
   public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }
}


