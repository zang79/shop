package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                          Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){ //상품 등록 시 필수 값이 없다면서 다시 상품 등록 페이지로 전환합니다.
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){ //첫번째 이미지 없다고 전환합니다.
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList); //저장 로직을 호출 매개변수로 상품 정보와 상품 이미지 정보를 담고
            //있는 itemFileList를 넘겨줍니다.
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/"; //상품이 정상적으로 등록되었다면 메인 페이지로 이동합니다.
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId); //조회한 상품 데이터를 모델에 담아서 뷰로 전달합니다.
            model.addAttribute("itemFormDto", itemFormDto);
        } catch(EntityNotFoundException e){ //엔티티가 존재하지 않을 경우 에러메시지를 담아서 상품 등록 페이지로 이동합니다.
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList); //상품수정 로직을 호출합니다.
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";
    }
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    //value에 상품 관리 화면 진입시 url에 페이지 번호가 없는 경우와 페이지 번호가 있는 경우 2가지를 매칭합니다.
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        //페이징을 위해서 pagerequest.of 메소드를 통해 peaeable 객체를 생성합니다.
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        //조회 조건과 페이징 정보를 파라미터로 넘겨서 page<item>객체를 반환 받습니다.
        model.addAttribute("items", items); //조회한 상품 데이터 및 페이징 정보를 뷰에 전달합니다.
        model.addAttribute("itemSearchDto", itemSearchDto);//페이지 전환시 기존 검색 조건을 유지한 채 이동할수있도록 뷰에 전달
        model.addAttribute("maxPage", 5);//상품관리 메뉴 하단에 보여줄 페이지 번호의 최대 객수입니다.

        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

}
