package com.shop.service;

import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}") //@value 이용하여 properties의 itemimglocation 값을 부러와서 itemimglocation 변수에 넣어줍니다.
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes()); //사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름,
            // 파일을 파일의 바이트 배열을 파일 업로드 파라미터로 uploadfile 메소드를 호출합니다. 호출 결과 로컬에 저장된 파일의
            // 이름을 imgName 변수에 저장합니다.
            imgUrl = "/images/item/" + imgName; //저장한 상품 이미지를 불러올 경로를 설정합니다. 외부 리소스를 부러오는 urlPatterns로
            //webmvcconfig 클래스에서 */images/**를 설정해주었습니다. 또한 application.properties에서 설정한 uploadpath 프로퍼티 경로인
            //c:/shop/"아래 item 폴더에 이미지를 저장하므로 상품 이미지를 부러오는 경로로 "/images/item/를 붙여줍니다.
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl); //입력받은 상품 이미지 정볼ㄹ 저장합니다.
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
            //상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회합니다.
                    .orElseThrow(EntityNotFoundException::new);
            //상품 등록화면으로부터 전달 받은 itemFormdto를 통해 상품 엔티티를 업데이트합니다.
            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())) { //기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제합니다.
                fileService.deleteFile(itemImgLocation + "/" +
                        savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            //업데이트한 상품 이미지 파일을 업로드합니다.
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
            //변경된 상품 이미지 정보를 세팅해줍니다.


        }
    }
}